package view.components;

import java.io.IOException;

public interface ButtonListener {
	public void onClickButton(Button btn) throws IOException, CloneNotSupportedException;
	public void redraw();
}
