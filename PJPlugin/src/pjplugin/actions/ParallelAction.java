package pjplugin.actions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

public class ParallelAction implements IObjectActionDelegate {

	@Override
	public void run(IAction _arg0) {
		IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getActivePage().getActiveEditor();

		if (part instanceof ITextEditor) {
			final ITextEditor editor = (ITextEditor) part;
			IEditorInput input = editor.getEditorInput();
			IFile file = ((FileEditorInput) input).getFile();

			String lineStr = null;
			StringBuilder sb = new StringBuilder();
			try {
				BufferedReader reader = new BufferedReader(new FileReader(file
						.getRawLocation().toFile()));
				lineStr = reader.readLine();

				while (lineStr != null) {
					sb.append(lineStr).append("\n");
					lineStr = reader.readLine();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			Document document = new org.eclipse.jface.text.Document(
					sb.toString());
			ASTParser parser = ASTParser.newParser(AST.JLS4);
			parser.setSource(document.get().toCharArray());
			parser.setKind(ASTParser.K_COMPILATION_UNIT);
			final CompilationUnit astRoot = (CompilationUnit) parser
					.createAST(null);
			AST ast = astRoot.getAST();

			final ASTRewrite rewriter = ASTRewrite.create(ast);
			astRoot.recordModifications();

			ISelection sel = editor.getSelectionProvider().getSelection();
			if (sel instanceof TextSelection) {
				final TextSelection textSel = (TextSelection) sel;
				final int lineNumber = textSel.getStartLine() + 1;
				final int endLineNum = textSel.getEndLine() + 1;
				
				System.out.println("startline:---------"+lineNumber +"endline:-----"+ endLineNum +"================================");
				
				astRoot.accept(new ASTVisitor() {
					
					public boolean visit(ExpressionStatement node){
						int startLine = astRoot.getLineNumber(node.getStartPosition());
						ListRewrite listRewrite = rewriter.getListRewrite(
								node.getParent(), Block.STATEMENTS_PROPERTY);
						
						System.out.println("------------>"+startLine);
						if(startLine == lineNumber){
							
							Statement placeHolder1 = (Statement) rewriter
									.createStringPlaceholder("//#omp parallel \n{ " ,ASTNode.EMPTY_STATEMENT);
							listRewrite.insertBefore(placeHolder1, node, null);
							
							} else if (startLine == endLineNum ){
								Statement placeHolder2 = (Statement) rewriter
										.createStringPlaceholder("}" ,ASTNode.EMPTY_STATEMENT);
								listRewrite.insertAfter(placeHolder2, node, null);
							}
						
						return true;
					}
					
//					public void endVisit(Statement node){
//						int startLineNum = astRoot.getLineNumber(node.getStartPosition());
//						System.out.println("------------>"+startLineNum);
//						if(startLineNum == endLineNum){
//							ListRewrite listRewrite = rewriter.getListRewrite(
//									node.getParent(), Block.STATEMENTS_PROPERTY);
//							Statement placeHolder1 = (Statement) rewriter
//									.createStringPlaceholder(" } " ,ASTNode.EMPTY_STATEMENT);
//							listRewrite.insertAfter(placeHolder1, node, null);
//						}
//					}
				});

				astRoot.accept(new ASTVisitor() {
					Set<String> declaredOutside = new HashSet<String>();
					Set<String> declaredInside = new HashSet<String>();
					Stack<NodeType> currentScope = new Stack<NodeType>();

					public void endVisit(ForStatement node) {
						int forLoopLinenum = astRoot.getLineNumber(node
								.getStartPosition());

						if (lineNumber == forLoopLinenum) {
							ListRewrite listRewrite = rewriter.getListRewrite(
									node.getParent(), Block.STATEMENTS_PROPERTY);

							String privateVar = "";
							if (declaredInside != null
									&& !declaredInside.isEmpty()) {
								privateVar = "private(";
								int count = 1;
								for (String in : declaredInside) {
									privateVar += in;
									if (count < declaredInside.size())
										privateVar += ",";
									count++;
								}
								privateVar += ")";
							}

							String sharedVar = "";
							if (declaredOutside != null
									&& !declaredOutside.isEmpty()) {
								sharedVar = "shared(";
								int count = 1;
								for (String in : declaredOutside) {
									sharedVar += in;
									if (count < declaredOutside.size())
										sharedVar += ",";
									count++;
								}
								sharedVar += ")";
							}

							Statement placeHolder = (Statement) rewriter
									.createStringPlaceholder(
											"//#omp parallel for " + privateVar
													+ " " + sharedVar,
											ASTNode.EMPTY_STATEMENT);
							listRewrite.insertBefore(placeHolder, node, null);

							System.out.println(astRoot.getLineNumber(node
									.getStartPosition()));
						}

						currentScope.pop();
					}

					public boolean visit(Assignment node) {
						if (node.getLocationInParent().getId()
								.equals("initializers")
								&& currentScope.peek() == NodeType.FOR) {
							currentScope.push(NodeType.FOR_INIT);
						}

						return true;
					}

					public boolean visit(ForStatement node) {
						currentScope.push(NodeType.FOR);
						return true;
					}

					public boolean visit(VariableDeclarationFragment node) {
						SimpleName name = node.getName();

						if (!currentScope.isEmpty()
								&& currentScope.peek() == NodeType.FOR) {
							declaredInside.add(name.getIdentifier());
						} else {
							declaredOutside.add(name.getIdentifier());
						}
						return true;
					}

					public boolean visit(SimpleName node) {
						if (!currentScope.isEmpty()
								&& currentScope.peek() == NodeType.FOR_INIT) {
							declaredOutside.remove(node.getIdentifier());
							declaredInside.add(node.getIdentifier());
							currentScope.pop();
						}
						return true;
					}
				});

				try {
					TextEdit edits = rewriter.rewriteAST(document, null);
					edits.apply(document);
					IDocumentProvider prov = editor.getDocumentProvider();
					IDocument doc = prov.getDocument(editor.getEditorInput());
					doc.set(document.get());
					
					editor.doSave(null);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	@Override
	public void selectionChanged(IAction _arg0, ISelection _arg1) {
	}

	@Override
	public void setActivePart(IAction arg0, IWorkbenchPart arg1) {
	}
}
