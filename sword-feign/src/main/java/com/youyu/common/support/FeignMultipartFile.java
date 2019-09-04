/*
 *    Copyright 2018-2019 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.youyu.common.support;

import org.apache.commons.fileupload.FileItem;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.Part;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * {@link MultipartFile} implementation for Feign Client FileUpload.
 *
 * @author WangSongJun
 * @date 2018-12-06
 */
public class FeignMultipartFile implements MultipartFile {
    private final String name;
    private String originalFilename;
    @Nullable
    private String contentType;
    private final byte[] content;

    /**
     * Create a new FeignMultipartFile with the given content.
     *
     * @param name    the name of the file
     * @param content the content of the file
     */
    public FeignMultipartFile(String name, String originalFilename, @Nullable byte[] content) {
        this(name, originalFilename, null, content);
    }

    /**
     * Create a new FeignMultipartFile with the given content.
     *
     * @param name          the name of the file
     * @param contentStream the content of the file as stream
     * @throws IOException if reading from the stream failed
     */
    public FeignMultipartFile(String name, String originalFilename, InputStream contentStream) throws IOException {
        this(name, originalFilename, null, FileCopyUtils.copyToByteArray(contentStream));
    }

    /**
     * Create a new FeignMultipartFile with the given content.
     *
     * @param name             the name of the file
     * @param originalFilename the original filename (as on the client's machine)
     * @param contentType      the content type (if known)
     * @param contentStream    the content of the file as stream
     * @throws IOException if reading from the stream failed
     */
    public FeignMultipartFile(String name, String originalFilename, @Nullable String contentType, InputStream contentStream) throws IOException {
        this(name, originalFilename, contentType, FileCopyUtils.copyToByteArray(contentStream));
    }

    /**
     * Create a new FeignMultipartFile with the given content.
     *
     * @param name             the name of the file
     * @param originalFilename the original filename (as on the client's machine)
     * @param contentType      the content type (if known)
     * @param content          the content of the file
     */
    public FeignMultipartFile(String name, String originalFilename, @Nullable String contentType, @Nullable byte[] content) {
        Assert.hasLength(name, "Name must not be null");
        Assert.hasLength(originalFilename, "OriginalFilename must not be null");
        this.name = name;
        this.originalFilename = originalFilename;
        this.contentType = contentType;
        this.content = (content != null ? content : new byte[0]);
    }

    /**
     * Return the name of the parameter in the multipart form.
     *
     * @return the name of the parameter (never {@code null} or empty)
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Return the original filename in the client's filesystem.
     * <p>This may contain path information depending on the browser used,
     * but it typically will not with any other than Opera.
     *
     * @return the original filename, or the empty String if no file has been chosen
     * in the multipart form, or {@code null} if not defined or not available
     * @see FileItem#getName()
     * @see CommonsMultipartFile#setPreserveFilename
     */
    @Override
    public String getOriginalFilename() {
        return this.originalFilename;
    }

    /**
     * Return the content type of the file.
     *
     * @return the content type, or {@code null} if not defined
     * (or no file has been chosen in the multipart form)
     */
    @Override
    public String getContentType() {
        return this.contentType;
    }

    /**
     * Return whether the uploaded file is empty, that is, either no file has
     * been chosen in the multipart form or the chosen file has no content.
     */
    @Override
    public boolean isEmpty() {
        return false;
    }

    /**
     * Return the size of the file in bytes.
     *
     * @return the size of the file, or 0 if empty
     */
    @Override
    public long getSize() {
        return this.content.length;
    }

    /**
     * Return the contents of the file as an array of bytes.
     *
     * @return the contents of the file as bytes, or an empty byte array if empty
     * @throws IOException in case of access errors (if the temporary store fails)
     */
    @Override
    public byte[] getBytes() throws IOException {
        return this.content;
    }

    /**
     * Return an InputStream to read the contents of the file from.
     * <p>The user is responsible for closing the returned stream.
     *
     * @return the contents of the file as stream, or an empty stream if empty
     * @throws IOException in case of access errors (if the temporary store fails)
     */
    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(this.content);
    }

    /**
     * Transfer the received file to the given destination file.
     * <p>This may either move the file in the filesystem, copy the file in the
     * filesystem, or save memory-held contents to the destination file. If the
     * destination file already exists, it will be deleted first.
     * <p>If the target file has been moved in the filesystem, this operation
     * cannot be invoked again afterwards. Therefore, call this method just once
     * in order to work with any storage mechanism.
     * <p><b>NOTE:</b> Depending on the underlying provider, temporary storage
     * may be container-dependent, including the base directory for relative
     * destinations specified here (e.g. with Servlet 3.0 multipart handling).
     * For absolute destinations, the target file may get renamed/moved from its
     * temporary location or newly copied, even if a temporary copy already exists.
     *
     * @param dest the destination file (typically absolute)
     * @throws IOException           in case of reading or writing errors
     * @throws IllegalStateException if the file has already been moved
     *                               in the filesystem and is not available anymore for another transfer
     * @see FileItem#write(File)
     * @see Part#write(String)
     */
    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        FileCopyUtils.copy(this.content, dest);
    }
}
