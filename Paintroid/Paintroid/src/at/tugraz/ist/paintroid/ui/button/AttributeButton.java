package at.tugraz.ist.paintroid.ui.button;

import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Cap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.TextView;
import at.tugraz.ist.paintroid.R;
import at.tugraz.ist.paintroid.dialog.DialogBrushPicker;
import at.tugraz.ist.paintroid.dialog.DialogBrushPicker.OnBrushChangedListener;
import at.tugraz.ist.paintroid.dialog.colorpicker.ColorPickerDialog;
import at.tugraz.ist.paintroid.dialog.colorpicker.ColorPickerDialog.OnColorPickedListener;
import at.tugraz.ist.paintroid.tools.Tool;
import at.tugraz.ist.paintroid.ui.Toolbar;

public class AttributeButton extends TextView implements OnClickListener, OnLongClickListener, Observer {

	protected Toolbar toolbar;

	public AttributeButton(Context context) {
		super(context);
		init(context);
	}

	public AttributeButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public AttributeButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	protected void init(Context context) {
		this.setOnClickListener(this);
		this.setOnLongClickListener(this);
	}

	public void setToolbar(Toolbar toolbar) {
		this.toolbar = toolbar;
		Observable tool = (Observable) toolbar.getCurrentTool();
		tool.addObserver(this);
		update((Observable) toolbar, null);
	}

	@Override
	public boolean onLongClick(View view) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onClick(final View view) {
		final Tool currentTool = toolbar.getCurrentTool();
		switch (this.getId()) {
		case R.id.btn_Parameter1:
			switch (currentTool.getToolType()) {
			case MAGIC:
			case CURSOR:
			case BRUSH:
			case PIPETTE:
				OnColorPickedListener colorPickerListener = new OnColorPickedListener() {
					@Override
					public void colorChanged(int color) {
						if (color == Color.TRANSPARENT) {
							Log.d("PAINTROID", "Transparent set");
							view.setBackgroundResource(R.drawable.transparent_64);
						}
						currentTool.changePaintColor(color);
					}
				};
				ColorPickerDialog colorpicker = new ColorPickerDialog(this.getContext(), colorPickerListener,
						currentTool.getDrawPaint().getColor());
				colorpicker.show();
				break;
			case FLOATINGBOX:
				// Rotate left
				// TODO
				// if (!((FloatingBoxTool) currentTool).rotate(-90)) {
				// Toast toast = Toast.makeText(this.getContext(),
				// R.string.warning_floating_box_rotate,
				// Toast.LENGTH_SHORT);
				// toast.show();
				// }
				break;
			}
			break;
		case R.id.btn_Parameter2:
			switch (currentTool.getToolType()) {
			case BRUSH:
			case CURSOR:
				OnBrushChangedListener mStroke = new OnBrushChangedListener() {
					@Override
					public void setCap(Cap cap) {
						currentTool.changePaintStrokeCap(cap);
					}

					@Override
					public void setStroke(int stroke) {
						currentTool.changePaintStrokeWidth(stroke);
					}
				};

				DialogBrushPicker strokepicker = new DialogBrushPicker(this.getContext(), mStroke);
				strokepicker.show();
				break;
			case FLOATINGBOX:
				// Rotate right
				// TODO
				// if (!((FloatingBoxTool) currentTool).rotate(90)) {
				// Toast toast = Toast.makeText(activity, R.string.warning_floating_box_rotate,
				// Toast.LENGTH_SHORT);
				// toast.show();
				// }
			}
			break;
		}
	}

	@Override
	public void update(Observable observable, Object argument) {
		if (observable instanceof Toolbar) {
			Observable tool = (Observable) toolbar.getCurrentTool();
			tool.addObserver(this);
		}
		final Tool currentTool = toolbar.getCurrentTool();
		int resource = currentTool.getAttributeButtonResource();
		if (resource == 0) {
			int color = currentTool.getAttributeButtonColor();
			this.setBackgroundColor(color);
		}
	}
}