/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.cloning.api;

import com.liferay.portal.kernel.exception.PortalException;

/**
 * @author Gergely Mathe
 */
public class CloningException extends PortalException {
	public CloningException() {
	}

	public CloningException(String msg) {
		super(msg);
	}

	public CloningException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public CloningException(Throwable cause) {
		super(cause);
	}

}
