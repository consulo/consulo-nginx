/*
 * Copyright 2020 General Electric Technology GmbH.  All Rights Reserved.
 * This document is the confidential and proprietary information of General Electric Technology GmbH and may not be reproduced, transmitted, stored,
 * or copied in whole or in part, or used to furnish information to others, without the prior written permission of
 * General Electric Technology GmbH or Grid Solutions.
 */
package net.ishchenko.idea.nginx;

import org.jetbrains.annotations.NotNull;

import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.fileTypes.FileTypeFactory;

/**
 * @author andrii.borovyk 08/28/2020
 */
public class NginxFileTypeFactory extends FileTypeFactory {

    @Override
    public void createFileTypes(@NotNull FileTypeConsumer consumer) {
        consumer.consume(NginxFileType.INSTANCE);
    }
}
