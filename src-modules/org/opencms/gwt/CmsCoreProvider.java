/*
 * File   : $Source: /alkacon/cvs/opencms/src-modules/org/opencms/gwt/Attic/CmsCoreProvider.java,v $
 * Date   : $Date: 2010/04/19 06:39:10 $
 * Version: $Revision: 1.6 $
 *
 * This library is part of OpenCms -
 * the Open Source Content Management System
 *
 * Copyright (c) 2002 - 2009 Alkacon Software GmbH (http://www.alkacon.com)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * For further information about Alkacon Software GmbH, please see the
 * company website: http://www.alkacon.com
 *
 * For further information about OpenCms, please see the
 * project website: http://www.opencms.org
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package org.opencms.gwt;

import org.opencms.file.CmsObject;
import org.opencms.flex.CmsFlexController;
import org.opencms.gwt.shared.I_CmsCoreProviderConstants;
import org.opencms.json.JSONException;
import org.opencms.json.JSONObject;
import org.opencms.main.CmsLog;
import org.opencms.main.OpenCms;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;

/**
 * Convenience class to provide core server-side data to the client.<p> 
 * 
 * @author Michael Moossen
 * 
 * @version $Revision: 1.6 $ 
 * 
 * @since 8.0.0
 * 
 * @see org.opencms.gwt.client.CmsCoreProvider
 */
public final class CmsCoreProvider implements I_CmsCoreProviderConstants, I_CmsCoreProvider {

    /** Internal instance. */
    private static I_CmsCoreProvider INSTANCE;

    /** Static reference to the log. */
    private static final Log LOG = CmsLog.getLog(CmsCoreProvider.class);

    /**
     * Hides the public constructor for this utility class.<p>
     */
    private CmsCoreProvider() {

        // hide the constructor
    }

    /**
     * Returns the client message instance.<p>
     * 
     * @return the client message instance
     */
    public static I_CmsCoreProvider get() {

        if (INSTANCE == null) {
            INSTANCE = new CmsCoreProvider();
        }
        return INSTANCE;
    }

    /**
     * @see org.opencms.gwt.I_CmsCoreProvider#export(javax.servlet.http.HttpServletRequest)
     */
    public String export(HttpServletRequest request) {

        StringBuffer sb = new StringBuffer();
        sb.append(DICT_NAME.replace('.', '_')).append("=").append(getData(request).toString()).append(";");
        sb.append(ClientMessages.get().export(request));
        return sb.toString();
    }

    /**
     * @see org.opencms.gwt.I_CmsCoreProvider#exportAll(javax.servlet.http.HttpServletRequest)
     */
    public String exportAll(HttpServletRequest request) {

        return export(request);
    }

    /**
     * @see org.opencms.gwt.I_CmsCoreProvider#getData(javax.servlet.http.HttpServletRequest)
     */
    public JSONObject getData(HttpServletRequest request) {

        CmsObject cms = CmsFlexController.getCmsObject(request);

        JSONObject keys = new JSONObject();
        try {
            keys.put(KEY_CONTEXT, OpenCms.getSystemInfo().getOpenCmsContext());
            keys.put(KEY_LOCALE, cms.getRequestContext().getLocale().toString());
            keys.put(KEY_SITE_ROOT, cms.getRequestContext().getSiteRoot());
            keys.put(KEY_WP_LOCALE, OpenCms.getWorkplaceManager().getWorkplaceLocale(cms).toString());
            keys.put(KEY_URI, cms.getRequestContext().getUri());
        } catch (Throwable e) {
            LOG.error(e.getLocalizedMessage(), e);
            try {
                keys.put("error", e.getLocalizedMessage());
            } catch (JSONException e1) {
                // ignore, should never happen
                LOG.error(e1.getLocalizedMessage(), e1);
            }
        }
        return keys;
    }
}
