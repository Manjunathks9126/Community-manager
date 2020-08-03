package com.ot.cm.util;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.ot.cm.exception.TGOCPRestException;

@Component
public class TGOCPAssert {

	public static void notNull(Object object, String message) throws TGOCPRestException {
		if (object == null) {
			throw new TGOCPRestException(HttpStatus.INTERNAL_SERVER_ERROR, "TGOCPAPP-500", "TGOCPAPP-500", message,
					null);
		}
	}

}
