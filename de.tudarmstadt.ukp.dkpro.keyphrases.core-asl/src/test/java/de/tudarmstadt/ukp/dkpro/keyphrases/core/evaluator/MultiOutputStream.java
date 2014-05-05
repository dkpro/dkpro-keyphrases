package de.tudarmstadt.ukp.dkpro.keyphrases.core.evaluator;

import java.io.IOException;
import java.io.OutputStream;

public class MultiOutputStream
    extends OutputStream
{
    final private OutputStream[] outputStreams;

    public MultiOutputStream(final OutputStream... outputStreams)
    {
        super();
        this.outputStreams = outputStreams;
    }

    @Override
    public void write(final int integer)
        throws IOException
    {
        for (final OutputStream out : outputStreams) {
            out.write(integer);
        }
    }

    @Override
    public void write(final byte[] byteArray)
        throws IOException
    {
        for (final OutputStream out : outputStreams) {
            out.write(byteArray);
        }
    }

    @Override
    public void write(final byte[] byteArray, final int off, final int len)
        throws IOException
    {
        for (final OutputStream out : outputStreams) {
            out.write(byteArray, off, len);
        }
    }

    @Override
    public void flush()
        throws IOException
    {
        for (final OutputStream out : outputStreams) {
            out.flush();
        }
    }

    @Override
    public void close()
        throws IOException
    {
        for (final OutputStream out : outputStreams) {
            out.close();
        }
    }
}
