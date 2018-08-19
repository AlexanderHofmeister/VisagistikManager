package de.visagistikmanager.view;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.beanutils.BeanUtils;

import de.visagistikmanager.model.BaseEntity;
import de.visagistikmanager.model.ModelAttribute;
import de.visagistikmanager.service.ClassUtil;
import de.visagistikmanager.view.components.YesNoRadioButtonGroup;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import lombok.Getter;

public abstract class BaseEditView<E extends BaseEntity> extends GridPane {

	@Getter
	private E model;

	protected Button saveButton = new Button("Speichern");
	protected Button cancelButton = new Button("Abbrechen");

	public void setModel(final E model) {
		this.model = model;

		List<Field> fields = Stream.of(model.getClass().getDeclaredFields())
				.filter(field -> field.isAnnotationPresent(ModelAttribute.class)).collect(Collectors.toList());

		try {
			for (Field field : fields) {

				for (Node node : getChildren()) {

					if (node.getId() != null) {
						if (node.getId().equals(field.getName())) {
							if (node instanceof TextField) {
								if (node instanceof DatePicker) {
									((DatePicker) node)
											.setValue(LocalDate.parse(BeanUtils.getProperty(model, field.getName())));
								} else {
									((TextField) node).setText(BeanUtils.getProperty(model, field.getName()));
								}
							} else if (node instanceof DatePicker) {
								BeanUtils.setProperty(model, field.getName(), ((DatePicker) node).getValue());
							} else if (node instanceof YesNoRadioButtonGroup) {
								((YesNoRadioButtonGroup) node).setValue(BeanUtils.getProperty(model, field.getName()));
							}
						}

					}
				}
			}
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void applyValuesToModel() {

		Class<Object> actualTypeBinding = ClassUtil.getActualTypeBinding(getClass(), BaseEditView.class, 0);

		for (Field field : actualTypeBinding.getDeclaredFields()) {

			if (field.isAnnotationPresent(ModelAttribute.class)) {
				ModelAttribute annotation = field.getAnnotation(ModelAttribute.class);

				for (Node node : getChildren()) {
					if (GridPane.getRowIndex(node) == annotation.row()
							&& GridPane.getColumnIndex(node) == annotation.column()) {
						try {
							if (node instanceof TextField) {
								BeanUtils.setProperty(model, field.getName(), ((TextField) node).getText());
							} else if (node instanceof DatePicker) {
								BeanUtils.setProperty(model, field.getName(), ((DatePicker) node).getValue());
							} else if (node instanceof YesNoRadioButtonGroup) {
								BeanUtils.setProperty(model, field.getName(),
										((YesNoRadioButtonGroup) node).getValue());
							}

						} catch (IllegalAccessException | InvocationTargetException e) {
							e.printStackTrace();
						}
						break;
					}
				}
			}
		}
	}

	public BaseEditView() {

		Class<Object> actualTypeBinding = ClassUtil.getActualTypeBinding(getClass(), BaseEditView.class, 0);

		for (Field field : actualTypeBinding.getDeclaredFields()) {

			if (field.isAnnotationPresent(ModelAttribute.class)) {
				ModelAttribute annotation = field.getAnnotation(ModelAttribute.class);

				int column = annotation.column();
				int row = annotation.row();
				if (field.getType() == String.class || field.getType() == Integer.class
						|| field.getType() == int.class) {
					TextField inputField = new TextField();
					inputField.setId(field.getName());
					inputField.setPromptText(annotation.placeholder());
					add(inputField, column, row);
				} else if (field.getType() == LocalDate.class) {
					DatePicker dateInputField = new DatePicker();
					dateInputField.setId(field.getName());
					dateInputField.setPromptText(annotation.placeholder());
					add(dateInputField, column, row);
				} else if (field.getType() == boolean.class) {
					add(new YesNoRadioButtonGroup(annotation.placeholder()), column, row);
				} else {
					throw new IllegalArgumentException("No case defined for " + field.getType());
				}

			}

		}

		add(saveButton, 0, getChildren().size(), 2, 1);
		add(cancelButton, 1, getChildren().size() - 1, 2, 1);
	}

	public void setSaveAction(EventHandler<ActionEvent> event) {
		saveButton.setOnAction(event);
	}

	public void setCancelAction(EventHandler<ActionEvent> event) {
		cancelButton.setOnAction(event);
	}
}
