package de.visagistikmanager.view;

import java.lang.reflect.InvocationTargetException;

import de.visagistikmanager.model.BaseEntity;
import de.visagistikmanager.model.Title;
import de.visagistikmanager.service.AbstractEntityService;
import de.visagistikmanager.service.ClassUtil;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;

public abstract class BaseEntityView<E extends BaseEntity> extends GridPane {

	protected abstract AbstractEntityService<E> getService();

	protected abstract BaseEditView<E> getEditView();

	protected abstract BaseListView<E> getListView();

	protected GridPane panel = new GridPane();

	private Button createButton = new Button();

	public BaseEntityView() {

		setHgap(15);
		setVgap(15);
		setPadding(new Insets(10, 10, 10, 10));

		Class<E> actualTypeBinding = ClassUtil.getActualTypeBinding(getClass(), BaseEntityView.class, 0);

		BaseEditView<E> editView = getEditView();
		createButton.setText(actualTypeBinding.getAnnotation(Title.class).value() + " anlegen");

		createButton.setOnAction(action -> {

			try {
				editView.setModel(actualTypeBinding.getDeclaredConstructor().newInstance());
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				// TODO log
				e.printStackTrace();
			}
			panel.getChildren().clear();
			panel.add(editView, 1, 1, 2, 2);
		});

		editView.setSaveAction(e -> {
			editView.applyValuesToModel();
			E model = editView.getModel();
			model = editView.applyCustomValuesToModel(model);
			model = getService().update(model);
			TableView<E> table = getListView().getTable();
			ObservableList<E> items = table.getItems();
			if (!items.contains(model)) {
				items.add(model);
			}
			table.refresh();
			panel.getChildren().remove(editView);
		});

		editView.setCancelAction(e -> {
			panel.getChildren().remove(editView);
		});

		add(createButton, 0, 0);
		add(getListView(), 0, 1);
		add(panel, 1, 1);
	}
}
