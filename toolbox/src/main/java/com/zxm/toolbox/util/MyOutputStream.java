package com.zxm.toolbox.util;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Objects;

public class MyOutputStream extends OutputStream {
	private final TextArea textArea;
	
	public MyOutputStream(TextArea textArea) {
		super();
		this.textArea = textArea;
	}

	@Override
	public void write(int b) {
		Runnable runnable = () -> textArea.appendText(String.valueOf(b));
		runnable.run();
	}
	@Override
	public void write(byte b[]) throws IOException {
		write(b, 0, b.length);
	}
	@Override
	public void write(byte b[], int off, int len) throws IOException {
		String s = new String(b, off, len);
		Platform.runLater(() -> textArea.appendText(s));
	}
}
