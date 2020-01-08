package com.coveros.training.autoinsurance;

/*******************************************************************************
 * Copyright (c) 2009, 2020 Mountainminds GmbH & Co. KG and Contributors
 * This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Marc R. Hoffmann - initial API and implementation
 *
 *******************************************************************************/

import org.jacoco.core.data.ExecutionDataWriter;
import org.jacoco.core.runtime.RemoteControlReader;
import org.jacoco.core.runtime.RemoteControlWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * This example connects to a coverage agent that run in output mode
 * <code>tcpserver</code> and requests execution data. The collected data is
 * dumped to a local file.
 */
public class ExecutionDataClient {

    private static final String ADDRESS = "localhost";

    /**
     * Starts the Jacoco execution data request.
     * First argument is the port to connect to, second is the file to write to.
     */
    public static void main(final String[] args) throws IOException {
        int port = Integer.parseInt(args[0]);
        String destfile = args[1];
        final FileOutputStream localFile = new FileOutputStream(destfile);
        final ExecutionDataWriter localWriter = new ExecutionDataWriter(
                localFile);

        // Open a socket to the coverage agent:
        try (Socket socket = new Socket(InetAddress.getByName(ADDRESS), port)) {
            final RemoteControlWriter writer = new RemoteControlWriter(
                    socket.getOutputStream());
            final RemoteControlReader reader = new RemoteControlReader(
                    socket.getInputStream());
            reader.setSessionInfoVisitor(localWriter);
            reader.setExecutionDataVisitor(localWriter);

            // Send a dump command and read the response:
            writer.visitDumpCommand(true, true);
            if (!reader.read()) {
                throw new IOException("Socket closed unexpectedly.");
            }

        }
        localFile.close();
    }

    private ExecutionDataClient() {
    }
}