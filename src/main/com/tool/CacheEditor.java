package com.tool;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Panel;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;

import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.ImageTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
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
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.wb.swt.SWTResourceManager;

import com.cache.CacheArchive;
import com.cache.CacheFile;
import com.cache.CacheStore;

/**
 * The {@code CacheEditor} allows for creating and modifying a
 * {@code CacheStore}.
 * 
 * @author Albert Beaupre
 */
public class CacheEditor {

	private MenuItem mntmSave, mntmSaveAs;
	private MenuItem mntmAddArchive, mntmRemoveArchive;
	private MenuItem mntmImportCache;

	private StyledText fileDataText;

	private Button dragAndDropButton;

	private Tree archiveTree;

	private boolean requiresSave;
	private File projectPath;
	private File saveFile;

	private JScrollPane tableScrollPane;

	private CacheArchive currentViewingArchive;
	private CacheFile currentViewingFile;
	private CacheStore store;

	protected Shell shell;
	private JTable table;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			CacheEditor window = new CacheEditor();
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

		shell.setMaximized(true);
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
	@SuppressWarnings("serial")
	protected void createContents() {
		shell = new Shell();
		shell.setImage(SWTResourceManager.getImage(CacheEditor.class, "/resources/icons/icon-100.png"));
		shell.setSize(800, 600);
		shell.setText("Cache Editor");
		shell.setLayout(new FillLayout());

		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);

		MenuItem mntmFile = new MenuItem(menu, SWT.CASCADE);
		mntmFile.setText("File");

		Menu fileMenu = new Menu(mntmFile);
		mntmFile.setMenu(fileMenu);

		MenuItem mntmNew = new MenuItem(fileMenu, SWT.NONE);
		mntmNew.setToolTipText("Create a new project");
		mntmNew.setImage(SWTResourceManager.getImage(CacheEditor.class, "/resources/icons/new-file.png"));
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
					CacheEditor.this.projectPath = new File(path);
					store = new CacheStore();
					mntmSave.setEnabled(true);
					mntmSaveAs.setEnabled(true);
					mntmAddArchive.setEnabled(true);
					mntmImportCache.setEnabled(true);
				}
			}
		});

		mntmImportCache = new MenuItem(fileMenu, SWT.NONE);
		mntmImportCache.setImage(SWTResourceManager.getImage(CacheEditor.class, "/resources/icons/open.png"));
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
						CacheEditor.this.saveFile = new File(filePath);
						CacheEditor.this.store = CacheStore.load(saveFile);

						archiveTree.clearAll(true);

						for (CacheArchive archive : store.getArchives()) {

							Image archiveImage = SWTResourceManager.getImage(CacheEditor.class, "/resources/icons/add-archive.png");

							TreeItem archiveItem = new TreeItem(archiveTree, SWT.V_SCROLL);
							archiveItem.setImage(archiveImage);
							archiveItem.setText("Archive " + archive.getIndex());
							archiveItem.setData(archive);

							for (CacheFile file : archive.getFiles()) {

								TreeItem treeItem = new TreeItem(archiveItem, SWT.V_SCROLL);
								treeItem.setText("File " + file.getIndex());
								treeItem.setData(file);
								treeItem.setChecked(true);
								treeItem.setImage(SWTResourceManager.getImage(CacheEditor.class, "/resources/icons/file-empty.png"));
							}
						}
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		new MenuItem(fileMenu, SWT.SEPARATOR);

		mntmSave = new MenuItem(fileMenu, SWT.NONE);
		mntmSave.setImage(SWTResourceManager.getImage(CacheEditor.class, "/resources/icons/save.png"));
		mntmSave.setText("Save\tCtrl + S");
		mntmSave.setAccelerator(SWT.CTRL + 'S');
		mntmSave.setEnabled(false);
		mntmSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					if (Objects.isNull(CacheEditor.this.saveFile)) {
						FileDialog dialog = new FileDialog(shell, SWT.SAVE);
						dialog.setFilterNames(new String[] { "Cache Files", "All Files (*.*)" });
						dialog.setFilterExtensions(new String[] { "*.cache", "*.*" });
						dialog.setFilterPath(projectPath.getPath());

						String filePath = dialog.open();

						if (Objects.nonNull(filePath)) {
							CacheEditor.this.saveFile = new File(filePath);
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

		mntmSaveAs = new MenuItem(fileMenu, SWT.NONE);
		mntmSaveAs.setImage(SWTResourceManager.getImage(CacheEditor.class, "/resources/icons/save-as.png"));
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
					CacheEditor.this.saveFile = new File(dialog.open());
					store.save(saveFile);
					requiresSave = false;
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		new MenuItem(fileMenu, SWT.SEPARATOR);

		MenuItem mntmExit = new MenuItem(fileMenu, SWT.NONE);
		mntmExit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (CacheEditor.this.requiresSave) {
					MessageBox messageBox = new MessageBox(shell, SWT.ICON_WARNING | SWT.YES | SWT.NO);

					messageBox.setText("Unsaved changes");
					messageBox.setMessage("You have unsaved changes, are you sure you want to exit without saving?");
					int result = messageBox.open();
					if (result == SWT.YES) {
						shell.dispose();
						System.exit(0);
					}
				} else {
					shell.dispose();
					System.exit(0);
				}
			}
		});
		mntmExit.setImage(SWTResourceManager.getImage(CacheEditor.class, "/resources/icons/close.png"));
		mntmExit.setText("Close");

		MenuItem mntmEdit = new MenuItem(menu, SWT.CASCADE);
		mntmEdit.setText("Edit");

		Menu editMenu = new Menu(mntmEdit);
		mntmEdit.setMenu(editMenu);

		mntmAddArchive = new MenuItem(editMenu, SWT.NONE);
		mntmAddArchive.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Image archiveImage = SWTResourceManager.getImage(CacheEditor.class, "/resources/icons/add-archive.png");

				CacheArchive archive = new CacheArchive(store.getArchives().length);

				TreeItem archiveItem = new TreeItem(archiveTree, SWT.V_SCROLL);
				archiveItem.setImage(archiveImage);
				archiveItem.setText("Archive " + archive.getIndex());
				archiveItem.setData(archive);

				store.addArchive(archive);
			}
		});
		mntmAddArchive.setText("Add Archive");
		mntmAddArchive.setImage(SWTResourceManager.getImage(CacheEditor.class, "/resources/icons/add-archive.png"));
		mntmAddArchive.setEnabled(false);

		mntmRemoveArchive = new MenuItem(editMenu, SWT.NONE);
		mntmRemoveArchive.setEnabled(false);
		mntmRemoveArchive.setText("Remove Archive");
		mntmRemoveArchive.setImage(SWTResourceManager.getImage(CacheEditor.class, "/resources/icons/delete-archive.png"));
		mntmRemoveArchive.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				store.removeArchive(currentViewingArchive.getIndex());
				archiveTree.removeAll();

				for (CacheArchive archive : store.getArchives()) {

					Image archiveImage = SWTResourceManager.getImage(CacheEditor.class, "/resources/icons/add-archive.png");

					TreeItem archiveItem = new TreeItem(archiveTree, SWT.V_SCROLL);
					archiveItem.setImage(archiveImage);
					archiveItem.setText("Archive " + archive.getIndex());
					archiveItem.setData(archive);

					for (CacheFile file : archive.getFiles()) {

						TreeItem treeItem = new TreeItem(archiveItem, SWT.V_SCROLL);
						treeItem.setText("File " + file.getIndex());
						treeItem.setData(file);
						treeItem.setChecked(true);
						treeItem.setImage(SWTResourceManager.getImage(CacheEditor.class, "/resources/icons/file-empty.png"));
					}
				}
			}
		});

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
										CacheFile file = new CacheFile(archive, archive.getFiles().length); //if no file found, it creates one

										TreeItem treeItem = new TreeItem(selection, SWT.V_SCROLL);
										treeItem.setText("File " + file.getIndex());
										treeItem.setData(file);
										treeItem.setChecked(true);
										treeItem.setImage(SWTResourceManager.getImage(CacheEditor.class, "/resources/icons/file-empty.png"));
										archiveTree.showItem(treeItem);
										archiveTree.setSelection(treeItem);
										archive.addFile(file);
									}
								});
								MenuItem deleteArchive = new MenuItem(menu, SWT.CASCADE);
								deleteArchive.setText("Remove Archive");
								deleteArchive.addSelectionListener(new SelectionAdapter() {
									@Override
									public void widgetSelected(SelectionEvent e) {
										archiveTree.removeAll();
										store.removeArchive(archive.getIndex());

										for (CacheArchive archive : store.getArchives()) {
											TreeItem archiveItem = new TreeItem(archiveTree, SWT.V_SCROLL);
											archiveItem.setText("Archive " + archive.getIndex());
											archiveItem.setData(archive);
											archiveItem.setImage(SWTResourceManager.getImage(CacheEditor.class, "/resources/icons/add-archive.png"));
											for (CacheFile file : archive.getFiles()) {
												TreeItem treeItem = new TreeItem(archiveItem, SWT.V_SCROLL);
												treeItem.setText("File " + file.getIndex());
												treeItem.setData(file);
												treeItem.setImage(SWTResourceManager.getImage(CacheEditor.class, "/resources/icons/file-empty.png"));
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
											treeItem.setImage(SWTResourceManager.getImage(CacheEditor.class, "/resources/icons/file-empty.png"));
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
						fileDataText.setText("");
					} else if (selection.getData() instanceof CacheFile) {
						CacheFile cacheFile = CacheFile.class.cast(selection.getData());

						displayFileProperties(cacheFile);

						dragAndDropButton.setVisible(true);
						table.setVisible(true);
					} else {
						dragAndDropButton.setVisible(false);
						table.setVisible(false);
						fileDataText.setText("");
					}

					mntmRemoveArchive.setEnabled(true);
				} else {
					dragAndDropButton.setVisible(false);
					mntmRemoveArchive.setEnabled(false);
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
		dragAndDropButton.setImage(SWTResourceManager.getImage(CacheEditor.class, "/resources/icons/drag-drop.png"));
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

						selection.setImage(SWTResourceManager.getImage(CacheEditor.class, "/resources/icons/file-full.png"));

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

						selection.setImage(SWTResourceManager.getImage(CacheEditor.class, "/resources/icons/file-full.png"));
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

		CLabel lblNewLabel = new CLabel(composite, SWT.NONE);
		lblNewLabel.setAlignment(SWT.CENTER);
		FormData fd_lblNewLabel = new FormData();
		fd_lblNewLabel.right = new FormAttachment(dragAndDropButton, -10, SWT.RIGHT);
		fd_lblNewLabel.left = new FormAttachment(dragAndDropButton, 10, SWT.LEFT);
		fd_lblNewLabel.top = new FormAttachment(0, 10);
		lblNewLabel.setLayoutData(fd_lblNewLabel);
		lblNewLabel.setText("File Properties");

		Label label = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		FormData fd_label = new FormData();
		fd_label.bottom = new FormAttachment(lblNewLabel, 13, SWT.BOTTOM);
		fd_label.left = new FormAttachment(lblNewLabel, 10, SWT.LEFT);
		fd_label.top = new FormAttachment(lblNewLabel, 11);
		fd_label.right = new FormAttachment(lblNewLabel, -10, SWT.RIGHT);
		label.setLayoutData(fd_label);

		Composite composite_1 = new Composite(composite, SWT.BORDER | SWT.EMBEDDED);
		FormData fd_composite_1 = new FormData();
		fd_composite_1.bottom = new FormAttachment(label, 106, SWT.BOTTOM);
		fd_composite_1.right = new FormAttachment(dragAndDropButton, 0, SWT.RIGHT);
		fd_composite_1.top = new FormAttachment(label, 18);
		fd_composite_1.left = new FormAttachment(lblNewLabel, 0, SWT.LEFT);
		composite_1.setLayoutData(fd_composite_1);

		Frame frame = SWT_AWT.new_Frame(composite_1);

		Panel panel = new Panel();
		frame.add(panel);
		panel.setLayout(new BorderLayout(0, 0));

		JRootPane rootPane = new JRootPane();
		panel.add(rootPane);

		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.setRowSelectionAllowed(false);
		table.setFillsViewportHeight(true);
		table.setBorder(null);
		table.getTableHeader().setReorderingAllowed(false);
		table.setModel(new DefaultTableModel(new Object[][] { { "Index", null }, { "Version", null }, { "Size", null }, { "Checksum", null } }, new String[] { "Property", "Value" }) {

			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 1 && row == 1;
			}
		});
		table.getModel().addTableModelListener(new TableModelListener() {

			@Override
			public void tableChanged(TableModelEvent e) {
				int column = e.getColumn();
				int row = e.getFirstRow();
				String value = (String) table.getValueAt(row, column);

				if (column == 1) {
					switch (row) {
					case 1:
						currentViewingFile.setVersion(Double.parseDouble(value));
						break;
					}
				}
			}

		});
		table.setVisible(false);

		tableScrollPane = new JScrollPane(table);
		tableScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		tableScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		tableScrollPane.setEnabled(false);
		rootPane.getContentPane().add(tableScrollPane, BorderLayout.NORTH);

		FormData updateChecksumForm = new FormData();
		updateChecksumForm.right = new FormAttachment(0, 311);
		updateChecksumForm.left = new FormAttachment(0, 20);

		scrolledCompositeRight.setContent(composite);
		scrolledCompositeRight.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		sashForm.setWeights(new int[] { 170, 611 });
	}

	private void displayFileProperties(CacheFile file) {
		currentViewingFile = file;
		table.setValueAt("" + file.getIndex(), 0, 1);
		table.setValueAt("" + file.getVersion(), 1, 1);
		table.setValueAt("" + String.format("%,d", file.getData().length) + " bytes", 2, 1);
		table.setValueAt("" + file.getChecksum(), 3, 1);
		table.setVisible(true);
	}

	private void displayArchiveProperties(CacheArchive archive) {
		currentViewingArchive = archive;
	}
}
