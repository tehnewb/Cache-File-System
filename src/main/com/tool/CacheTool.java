package com.tool;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.Stack;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.ImageTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.cache.CacheArchive;
import com.cache.CacheFile;
import com.cache.CacheStore;

public class CacheTool {

	private MenuItem mntmUndo, mntmRedo;
	private MenuItem mntmSave, mntmSaveAs;
	private MenuItem mntmAddArchive, mntmRemoveArchive;

	private Table table;
	private TableColumn propertyColumn, propertyValueColumn;
	private TableItem tableIndexItem, tableVersionItem, tableChecksumItem, tableFileSizeItem, tableFileNameItem;

	private StyledText fileDataText;
	private Button dragAndDropButton;

	private MenuItem mntmImportCache;

	private Label tableTitleLabel;

	private Tree archiveTree;

	private Stack<UndoCommand> undoStack;
	private Stack<UndoCommand> redoStack;

	private boolean requiresSave;
	private File projectPath;
	private File saveFile;

	private CacheStore store;

	protected Shell shell;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			CacheTool window = new CacheTool();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();

		createContents();

		shell.open();
		shell.layout();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		undoStack = new Stack<>();
		redoStack = new Stack<>();
		shell = new Shell();
		shell.setImage(SWTResourceManager.getImage(CacheTool.class, "/resources/icons/icon-100.png"));
		shell.setSize(800, 600);
		shell.setText("Cache Editor");
		shell.setLayout(new FillLayout());

		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);

		MenuItem mntmFile = new MenuItem(menu, SWT.CASCADE);
		mntmFile.setText("File");

		Menu menu_1 = new Menu(mntmFile);
		mntmFile.setMenu(menu_1);

		MenuItem mntmNew = new MenuItem(menu_1, SWT.NONE);
		mntmNew.setToolTipText("Create a new project");
		mntmNew.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {}
		});
		mntmNew.setImage(SWTResourceManager.getImage(CacheTool.class, "/resources/icons/new-file.png"));
		mntmNew.setText("New Project\t Ctrl + Shift + N");
		mntmNew.setAccelerator(SWT.CTRL + SWT.SHIFT + 'N');
		mntmNew.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dialog = new DirectoryDialog(shell, SWT.OPEN);
				dialog.setFilterPath(System.getProperty("user.home"));
				dialog.setText("Project Directory Selection");
				dialog.setMessage("Select a directory for the project");

				String path = dialog.open();
				if (Objects.nonNull(path)) {
					CacheTool.this.projectPath = new File(path);
					store = new CacheStore();
					mntmSave.setEnabled(true);
					mntmSaveAs.setEnabled(true);
					mntmAddArchive.setEnabled(true);
					mntmImportCache.setEnabled(true);
				}
			}
		});

		mntmImportCache = new MenuItem(menu_1, SWT.NONE);
		mntmImportCache.setImage(SWTResourceManager.getImage(CacheTool.class, "/resources/icons/open.png"));
		mntmImportCache.setText("Import Cache...\t Ctrl + Shift + O");
		mntmImportCache.setAccelerator(SWT.CTRL + SWT.SHIFT + 'O');
		mntmImportCache.setEnabled(false);

		mntmImportCache.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					FileDialog dialog = new FileDialog(shell, SWT.OPEN);
					dialog.setFilterNames(new String[] { "Cache Files", "All Files (*.*)" });
					dialog.setFilterExtensions(new String[] { "*.cache", "*.*" });
					dialog.setFilterPath(projectPath.getPath());

					String filePath = dialog.open();

					if (Objects.nonNull(filePath)) {
						CacheTool.this.saveFile = new File(filePath);
						CacheTool.this.store = CacheStore.load(saveFile);

						archiveTree.clearAll(true);

						for (CacheArchive archive : store.getArchives()) {

							Image archiveImage = SWTResourceManager.getImage(CacheTool.class, "/resources/icons/add-archive.png");

							TreeItem archiveItem = new TreeItem(archiveTree, SWT.V_SCROLL);
							archiveItem.setImage(archiveImage);
							archiveItem.setText("Archive " + archive.getIndex());
							archiveItem.setData(archive);

							for (CacheFile file : archive.getFiles()) {

								TreeItem treeItem = new TreeItem(archiveItem, SWT.V_SCROLL);
								treeItem.setText("File " + file.getIndex());
								treeItem.setData(file);
								treeItem.setChecked(true);
								treeItem.setImage(SWTResourceManager.getImage(CacheTool.class, "/resources/icons/file-empty.png"));
							}
						}
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		new MenuItem(menu_1, SWT.SEPARATOR);

		mntmSave = new MenuItem(menu_1, SWT.NONE);
		mntmSave.setImage(SWTResourceManager.getImage(CacheTool.class, "/resources/icons/save.png"));
		mntmSave.setText("Save\tCtrl + S");
		mntmSave.setAccelerator(SWT.CTRL + 'S');
		mntmSave.setEnabled(false);
		mntmSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					if (Objects.isNull(CacheTool.this.saveFile)) {
						FileDialog dialog = new FileDialog(shell, SWT.SAVE);
						dialog.setFilterNames(new String[] { "Cache Files", "All Files (*.*)" });
						dialog.setFilterExtensions(new String[] { "*.cache", "*.*" });
						dialog.setFilterPath(projectPath.getPath());

						String filePath = dialog.open();

						if (Objects.nonNull(filePath)) {
							CacheTool.this.saveFile = new File(filePath);
							store.save(saveFile);
						}
					} else {
						store.save(saveFile);
					}

					requiresSave = false;
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		mntmSaveAs = new MenuItem(menu_1, SWT.NONE);
		mntmSaveAs.setImage(SWTResourceManager.getImage(CacheTool.class, "/resources/icons/save-as.png"));
		mntmSaveAs.setText("Save As...\tCtrl + Shift + S");
		mntmSaveAs.setAccelerator(SWT.CTRL + SWT.SHIFT + 'S');
		mntmSaveAs.setEnabled(false);
		mntmSaveAs.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					FileDialog dialog = new FileDialog(shell, SWT.SAVE);
					dialog.setFilterNames(new String[] { "Cache Files", "All Files (*.*)" });
					dialog.setFilterExtensions(new String[] { "*.cache", "*.*" });
					dialog.setFilterPath(projectPath.getPath());
					CacheTool.this.saveFile = new File(dialog.open());
					store.save(saveFile);
					requiresSave = false;
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		new MenuItem(menu_1, SWT.SEPARATOR);

		MenuItem mntmExit = new MenuItem(menu_1, SWT.NONE);
		mntmExit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (CacheTool.this.requiresSave) {
					MessageBox messageBox = new MessageBox(shell, SWT.ICON_WARNING | SWT.YES | SWT.NO);

					messageBox.setText("Unsaved changes");
					messageBox.setMessage("You have unsaved changes, are you sure you want to exit without saving?");
					int result = messageBox.open();
					if (result == SWT.YES) {
						System.exit(0);
					}
				}
			}
		});
		mntmExit.setImage(SWTResourceManager.getImage(CacheTool.class, "/resources/icons/close.png"));
		mntmExit.setText("Close");

		MenuItem mntmEdit = new MenuItem(menu, SWT.CASCADE);
		mntmEdit.setText("Edit");

		Menu menu_2 = new Menu(mntmEdit);
		mntmEdit.setMenu(menu_2);

		mntmUndo = new MenuItem(menu_2, SWT.NONE);
		mntmUndo.setText("Undo\tCtrl + Z");
		mntmUndo.setEnabled(false);
		mntmUndo.setAccelerator(SWT.CTRL + 'Z');
		mntmUndo.setImage(SWTResourceManager.getImage(CacheTool.class, "/resources/icons/undo.png"));
		mntmUndo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				undo();
			}
		});

		mntmRedo = new MenuItem(menu_2, SWT.NONE);
		mntmRedo.setText("Redo\tCtrl + Y");
		mntmRedo.setAccelerator(SWT.CTRL + 'Y');
		mntmRedo.setEnabled(false);
		mntmRedo.setImage(SWTResourceManager.getImage(CacheTool.class, "/resources/icons/redo.png"));
		mntmRedo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				redo();
			}
		});

		mntmAddArchive = new MenuItem(menu_2, SWT.NONE);
		mntmAddArchive.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				UndoCommand command = new UndoCommand() {
					String commandName;
					TreeItem archiveItem;

					@Override
					public void execute() {
						Image archiveImage = SWTResourceManager.getImage(CacheTool.class, "/resources/icons/add-archive.png");

						CacheArchive archive = new CacheArchive(store.getArchives().length);

						archiveItem = new TreeItem(archiveTree, SWT.V_SCROLL);
						archiveItem.setImage(archiveImage);
						archiveItem.setText("Archive " + archive.getIndex());
						archiveItem.setData(archive);
						commandName = "Add archive " + archive.getIndex();

						store.addArchive(archive);
					}

					@Override
					public void undo() {
						archiveItem.dispose();
					}

					@Override
					public void redo() {
						execute();
					}

					@Override
					public String command() {
						return commandName;
					}

				};
				execute(command);
			}
		});
		mntmAddArchive.setText("Add Archive");
		mntmAddArchive.setImage(SWTResourceManager.getImage(CacheTool.class, "/resources/icons/add-archive.png"));
		mntmAddArchive.setEnabled(false);

		mntmRemoveArchive = new MenuItem(menu_2, SWT.NONE);
		mntmRemoveArchive.setEnabled(false);
		mntmRemoveArchive.setText("Remove Archive");
		mntmRemoveArchive.setImage(SWTResourceManager.getImage(CacheTool.class, "/resources/icons/delete-archive.png"));

		SashForm sashForm = new SashForm(shell, SWT.NONE);
		sashForm.setLayout(new FillLayout());

		ScrolledComposite scrolledCompositeLeft = new ScrolledComposite(sashForm, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledCompositeLeft.setExpandHorizontal(true);
		scrolledCompositeLeft.setExpandVertical(true);

		archiveTree = new Tree(scrolledCompositeLeft, SWT.V_SCROLL);
		archiveTree.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseDown(MouseEvent e) {
				if (e.button == 3) { // right click
					if (archiveTree.getSelectionCount() > 0) {
						TreeItem selection = archiveTree.getSelection()[0];
						if (Objects.nonNull(selection)) {
							if (CacheArchive.class.isInstance(selection.getData())) {
								CacheArchive archive = CacheArchive.class.cast(selection.getData());
								Menu menu = new Menu(archiveTree);
								MenuItem addFile = new MenuItem(menu, SWT.CASCADE);
								addFile.setText("Add File");
								addFile.addSelectionListener(new SelectionAdapter() {
									@Override
									public void widgetSelected(SelectionEvent e) {
										CacheFile file = new CacheFile(archive.getFiles().length); //if no file found, it creates one

										TreeItem treeItem = new TreeItem(selection, SWT.V_SCROLL);
										treeItem.setText("File " + file.getIndex());
										treeItem.setData(file);
										treeItem.setChecked(true);
										treeItem.setImage(SWTResourceManager.getImage(CacheTool.class, "/resources/icons/file-empty.png"));
										archiveTree.showItem(treeItem);
										archiveTree.setSelection(treeItem);
										archive.addFile(file);
									}
								});
								MenuItem deleteArchive = new MenuItem(menu, SWT.CASCADE);
								deleteArchive.setText("Delete Archive");
								deleteArchive.addSelectionListener(new SelectionAdapter() {
									@Override
									public void widgetSelected(SelectionEvent e) {
										archiveTree.removeAll();
										store.removeArchive(archive.getIndex());

										for (CacheArchive archive : store.getArchives()) {
											TreeItem archiveItem = new TreeItem(archiveTree, SWT.V_SCROLL);
											archiveItem.setText("Archive " + archive.getIndex());
											archiveItem.setData(archive);
											archiveItem.setImage(SWTResourceManager.getImage(CacheTool.class, "/resources/icons/add-archive.png"));
											for (CacheFile file : archive.getFiles()) {
												TreeItem treeItem = new TreeItem(archiveItem, SWT.V_SCROLL);
												treeItem.setText("File " + file.getIndex());
												treeItem.setData(file);
												treeItem.setImage(SWTResourceManager.getImage(CacheTool.class, "/resources/icons/file-empty.png"));
											}
										}
									}
								});
								archiveTree.setMenu(menu);
							} else if (CacheFile.class.isInstance(selection.getData())) {
								CacheFile file = CacheFile.class.cast(selection.getData());
								Menu menu = new Menu(archiveTree);
								MenuItem deleteFile = new MenuItem(menu, SWT.CASCADE);
								deleteFile.setText("Delete File");
								deleteFile.addSelectionListener(new SelectionAdapter() {
									@Override
									public void widgetSelected(SelectionEvent e) {
										TreeItem parent = selection.getParentItem();
										CacheArchive archive = CacheArchive.class.cast(parent.getData());
										archive.removeFile(file.getIndex());
										Arrays.stream(parent.getItems()).forEach(TreeItem::dispose);

										for (CacheFile file : archive.getFiles()) {
											TreeItem treeItem = new TreeItem(parent, SWT.V_SCROLL);
											treeItem.setText("File " + file.getIndex());
											treeItem.setData(file);
											treeItem.setChecked(true);
											treeItem.setImage(SWTResourceManager.getImage(CacheTool.class, "/resources/icons/file-empty.png"));
										}
									}
								});
								archiveTree.setMenu(menu);
							}
						}
					}
				}
			}

		});
		archiveTree.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (archiveTree.getSelectionCount() > 0) {
					TreeItem selection = archiveTree.getSelection()[0];
					if (selection.getData() instanceof CacheArchive) {
						CacheArchive archive = CacheArchive.class.cast(selection.getData());

						displayArchiveProperties(archive);

						dragAndDropButton.setVisible(false);
						table.setVisible(false);
						tableTitleLabel.setVisible(false);
						fileDataText.setText("");
					} else if (selection.getData() instanceof CacheFile) {
						CacheFile archiveFile = CacheFile.class.cast(selection.getData());
						dragAndDropButton.setVisible(true);
						tableTitleLabel.setVisible(true);
						table.setVisible(true);
						table.showColumn(propertyColumn);
						table.showColumn(propertyValueColumn);

						displayFileProperties(archiveFile);
					} else {
						dragAndDropButton.setVisible(false);
						tableTitleLabel.setVisible(false);
						table.setVisible(false);
						fileDataText.setText("");
					}

					mntmRemoveArchive.setEnabled(true);
				} else {
					dragAndDropButton.setVisible(false);
					mntmRemoveArchive.setEnabled(false);
					tableTitleLabel.setVisible(false);
					table.setVisible(false);

					fileDataText.setText("");
				}
			}

		});

		scrolledCompositeLeft.setContent(archiveTree);

		ScrolledComposite scrolledCompositeRight = new ScrolledComposite(sashForm, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledCompositeRight.setExpandHorizontal(true);
		scrolledCompositeRight.setExpandVertical(true);

		Composite composite = new Composite(scrolledCompositeRight, SWT.NONE);
		composite.setLayout(new FormLayout());

		dragAndDropButton = new Button(composite, SWT.CENTER);
		dragAndDropButton.setImage(SWTResourceManager.getImage(CacheTool.class, "/resources/icons/drag-drop.png"));
		dragAndDropButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(shell, SWT.OPEN);
				dialog.setFilterPath(System.getProperty("user.home"));

				String filePath = dialog.open();

				if (Objects.nonNull(filePath)) {
					try {
						byte[] fileData = Files.readAllBytes(Paths.get(filePath));
						TreeItem selection = archiveTree.getSelection()[0];
						CacheFile file = CacheFile.class.cast(selection.getData());
						file.setData(fileData);

						StringBuilder builder = new StringBuilder();
						String fileSize = String.format("%,d", file.getData().length);
						builder.append("File Size: " + fileSize + " bytes\n");
						builder.append("File Name: " + filePath);

						fileDataText.setText(builder.toString());

						selection.setImage(SWTResourceManager.getImage(CacheTool.class, "/resources/icons/file-full.png"));

						displayFileProperties(file);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		DropTarget target = new DropTarget(dragAndDropButton, DND.DROP_COPY | DND.DROP_LINK | DND.DROP_DEFAULT);
		target.setTransfer(new Transfer[] { FileTransfer.getInstance(), ImageTransfer.getInstance() });

		target.addDropListener(new DropTargetAdapter() {

			@Override
			public void drop(DropTargetEvent event) {
				if (event.data instanceof String[]) {
					String[] filenames = (String[]) event.data;
					try {
						byte[] fileData = Files.readAllBytes(Paths.get(filenames[0]));
						TreeItem selection = archiveTree.getSelection()[0];
						CacheFile file = CacheFile.class.cast(selection.getData());
						file.setData(fileData);

						StringBuilder builder = new StringBuilder();
						String fileSize = String.format("%,d", file.getData().length);
						builder.append("File Size: " + fileSize + " bytes\n");
						builder.append("File Name: " + filenames[0]);

						fileDataText.setText(builder.toString());

						selection.setImage(SWTResourceManager.getImage(CacheTool.class, "/resources/icons/file-full.png"));
						displayFileProperties(file);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else if (event.data instanceof ImageData) {

				}
			}

			public void dragEnter(DropTargetEvent event) {
				event.detail = DND.DROP_COPY;
			}
		});
		FormData fd_btnNewButton = new FormData();
		fd_btnNewButton.top = new FormAttachment(100, -182);
		fd_btnNewButton.right = new FormAttachment(100, -10);
		fd_btnNewButton.bottom = new FormAttachment(100, -10);
		fd_btnNewButton.left = new FormAttachment(0, 10);
		dragAndDropButton.setLayoutData(fd_btnNewButton);
		dragAndDropButton.setText("Drag Files Here");
		dragAndDropButton.setVisible(false);
		dragAndDropButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

			}
		});

		fileDataText = new StyledText(composite, SWT.NONE);
		fileDataText.setEditable(false);
		fileDataText.setEnabled(false);
		fileDataText.setAlignment(SWT.CENTER);
		FormData fileDataTextForm = new FormData();
		fileDataTextForm.top = new FormAttachment(dragAndDropButton, -43, SWT.TOP);
		fileDataTextForm.right = new FormAttachment(0, 597);
		fileDataTextForm.bottom = new FormAttachment(dragAndDropButton, -10);
		fileDataTextForm.left = new FormAttachment(0, 10);
		fileDataText.setLayoutData(fileDataTextForm);

		table = new Table(composite, SWT.FULL_SELECTION);
		table.setFont(SWTResourceManager.getFont("Arial Baltic", 9, SWT.NORMAL));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setVisible(false);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				TableItem item = table.getItem(new Point(e.x, e.y));
				if (e.button == 3 && Objects.nonNull(item)) {
					Menu menu = new Menu(table);
					MenuItem copyItem = new MenuItem(menu, SWT.CASCADE);
					copyItem.setText("Copy");
					copyItem.addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(SelectionEvent e) {
							Clipboard clipboard = new Clipboard(Display.getDefault());
							String selectedText = table.getSelection()[0].getText(1);

							clipboard.setContents(new Object[] { selectedText.toString() }, new Transfer[] { TextTransfer.getInstance() });
							clipboard.dispose();
						}
					});

					table.setMenu(menu);
					menu.setVisible(true);
				}
			}
		});

		FormData tableForm = new FormData();
		tableForm.right = new FormAttachment(dragAndDropButton, 0, SWT.RIGHT);
		tableForm.bottom = new FormAttachment(0, 155);
		tableForm.top = new FormAttachment(0, 36);
		tableForm.left = new FormAttachment(0, 10);
		table.setLayoutData(tableForm);

		tableTitleLabel = new Label(composite, SWT.NONE);
		tableTitleLabel.setAlignment(SWT.CENTER);
		tableTitleLabel.setVisible(false);
		FormData dragAndDropForm = new FormData();
		dragAndDropForm.bottom = new FormAttachment(table, -6);
		dragAndDropForm.right = new FormAttachment(dragAndDropButton, 0, SWT.RIGHT);

		propertyColumn = new TableColumn(table, SWT.CENTER);
		propertyColumn.setWidth(126);
		propertyColumn.setText("Property");

		tableIndexItem = new TableItem(table, SWT.NONE);
		tableIndexItem.setText("Index");
		tableIndexItem.setData("uneditable");

		tableVersionItem = new TableItem(table, SWT.NONE);
		tableVersionItem.setText("Version");

		tableFileSizeItem = new TableItem(table, SWT.NONE);
		tableFileSizeItem.setText("Size");
		tableFileSizeItem.setData("uneditable");

		propertyValueColumn = new TableColumn(table, SWT.CENTER);
		propertyValueColumn.setWidth(460);
		propertyValueColumn.setText("Value");

		tableFileNameItem = new TableItem(table, SWT.NONE);
		tableFileNameItem.setText("Name");

		tableChecksumItem = new TableItem(table, SWT.NONE);
		tableChecksumItem.setText("Checksum");

		final TableEditor editor = new TableEditor(table);

		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;
		table.addListener(SWT.MouseDown, event -> {
			Point pt = new Point(event.x, event.y);
			TableItem item = table.getItem(pt);
			if (Objects.nonNull(item)) {
				final int column = 1;
				final Text text = new Text(table, SWT.NONE);
				Listener textListener = e -> {
					switch (e.type) {
					case SWT.FocusOut:
						item.setText(column, text.getText());

						CacheFile file = CacheFile.class.cast(archiveTree.getSelection()[0].getData());
						try {
							switch (item.getText(0)) {
							//TODO case "Name" -> file.setFileName(text.getText());
							case "Version" -> file.setVersion(Double.parseDouble(text.getText()));
							case "Checksum" -> file.setChecksum(Integer.parseInt(text.getText()));
							}
						} catch (Exception ex) {
							MessageBox messageBox = new MessageBox(shell, SWT.OK);
							messageBox.setMessage(ex.getLocalizedMessage());
							messageBox.setText("Error Updating Table");
						}

						text.dispose();
						break;
					case SWT.Traverse:
						switch (e.detail) {
						case SWT.TRAVERSE_RETURN:
							item.setText(column, text.getText());

							file = CacheFile.class.cast(archiveTree.getSelection()[0].getData());
							try {
								switch (item.getText(0)) {
								/*
								 * TODO case "Name" -> { file.setFileName(text.getText());
								 * currentViewingTreeItem.setText(file.getFileName()); }
								 */
								case "Version" -> file.setVersion(Double.parseDouble(text.getText()));
								case "Checksum" -> file.setChecksum(Integer.parseInt(text.getText()));
								}
							} catch (Exception ex) {
								MessageBox messageBox = new MessageBox(shell, SWT.OK);
								messageBox.setMessage(ex.getMessage());
								messageBox.setText("Error Updating Table");
								messageBox.open();
							}
							//FALL THROUGH
						case SWT.TRAVERSE_ESCAPE:
							text.dispose();
							e.doit = false;
						}
						break;
					}
				};
				text.addListener(SWT.FocusOut, textListener);
				text.addListener(SWT.Traverse, textListener);
				editor.setEditor(text, item, column);
				text.setText(item.getText(column));
				text.selectAll();
				text.setFocus();
			}
		});

		dragAndDropForm.left = new FormAttachment(dragAndDropButton, 0, SWT.LEFT);
		tableTitleLabel.setLayoutData(dragAndDropForm);
		tableTitleLabel.setText("File Properties");
		scrolledCompositeRight.setContent(composite);
		scrolledCompositeRight.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		sashForm.setWeights(new int[] { 170, 611 });
	}

	private void displayFileProperties(CacheFile file) {
		tableIndexItem.setText(1, "" + file.getIndex());
		tableFileNameItem.setText(1, "File " + file.getIndex());
		tableFileSizeItem.setText(1, "" + file.getData().length);
		tableVersionItem.setText(1, "" + file.getVersion());
		tableChecksumItem.setText(1, "" + file.getChecksum());
	}

	private void displayArchiveProperties(CacheArchive archive) {

	}

	public void undo() {
		UndoCommand command = undoStack.pop();
		command.undo();
		redoStack.add(command);

		refreshCommandStack();
		this.requiresSave = true;
	}

	public void redo() {
		UndoCommand command = redoStack.pop();
		command.redo();
		undoStack.add(command);
		refreshCommandStack();
		this.requiresSave = true;
	}

	public void execute(UndoCommand command) {
		command.execute();
		undoStack.add(command);

		refreshCommandStack();

		this.requiresSave = true;
	}

	private void refreshCommandStack() {
		if (undoStack.isEmpty()) {
			this.mntmUndo.setEnabled(false);
		} else {
			this.mntmUndo.setEnabled(true);
			this.mntmUndo.setText("Undo\tCtrl + Z   " + undoStack.peek().command());
		}
		if (redoStack.isEmpty()) {
			this.mntmRedo.setEnabled(false);
		} else {
			this.mntmRedo.setEnabled(true);
			this.mntmRedo.setText("Redo\tCtrl + Y   " + redoStack.peek().command());
		}

	}

	private interface UndoCommand {
		void execute();

		void undo();

		void redo();

		String command();
	}
}
