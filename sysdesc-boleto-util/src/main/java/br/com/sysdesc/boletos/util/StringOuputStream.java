package br.com.sysdesc.boletos.util;

import java.io.IOException;
import java.io.OutputStream;

public class StringOuputStream extends OutputStream {

    private StringBuilder string = new StringBuilder();

    @Override
    public void write(int b) throws IOException {
        this.string.append((char) b);
    }

    @Override
    public String toString() {
        return this.string.toString();
    }
}
