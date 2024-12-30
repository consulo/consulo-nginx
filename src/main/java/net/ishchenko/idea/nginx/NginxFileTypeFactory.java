/*
 * Copyright 2020 General Electric Technology GmbH.  All Rights Reserved.
 * This document is the confidential and proprietary information of General Electric Technology GmbH and may not be reproduced, transmitted, stored,
 * or copied in whole or in part, or used to furnish information to others, without the prior written permission of
 * General Electric Technology GmbH or Grid Solutions.
 */
package net.ishchenko.idea.nginx;

import consulo.annotation.component.ExtensionImpl;
import consulo.virtualFileSystem.fileType.FileTypeConsumer;
import consulo.virtualFileSystem.fileType.FileTypeFactory;

import jakarta.annotation.Nonnull;

/**
 * @author andrii.borovyk 08/28/2020
 */
@ExtensionImpl
public class NginxFileTypeFactory extends FileTypeFactory {

    @Override
    public void createFileTypes(@Nonnull FileTypeConsumer consumer) {
        consumer.consume(NginxFileType.INSTANCE);
    }
}
