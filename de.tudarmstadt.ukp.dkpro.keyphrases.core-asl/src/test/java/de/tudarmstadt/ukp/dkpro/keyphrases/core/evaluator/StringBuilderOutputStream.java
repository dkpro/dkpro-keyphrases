package de.tudarmstadt.ukp.dkpro.keyphrases.core.evaluator;

import java.io.IOException;
import java.io.OutputStream;

public class StringBuilderOutputStream
extends OutputStream
{

private final StringBuilder stringBuilder;

public StringBuilderOutputStream(final StringBuilder stringBuilder)
{
    super();
    this.stringBuilder = stringBuilder;
}

@Override
public void write(final int integer)
    throws IOException
{
    stringBuilder.append((char) integer);
}

@Override
public void write(final byte[] byteArray)
    throws IOException
{
    for (final byte b : byteArray) {
        stringBuilder.append((char) b);
    }
}

@Override
public void write(final byte[] byteArray, final int off, final int len)
    throws IOException
{
    for (int i = off; i < len; ++i) {
        stringBuilder.append((char) byteArray[i]);
    }
}

}
