package net.sourceforge.squirrel_sql.client.plugin;
/*
 * Copyright (C) 2001 Colin Bell
 * colbell@users.sourceforge.net
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
import net.sourceforge.squirrel_sql.client.session.ISession;
import net.sourceforge.squirrel_sql.client.session.properties.ISessionPropertiesPanel;

/**
 * Base interface for all plugins associated with a session.
 */
public interface ISessionPlugin extends IPlugin {
	/**
	 * A new session has been created. At this point the
	 * <TT>SessionSheet</TT> does not exist for the new session.
	 *
	 * @param   session	 The new session.
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown if a <TT>null</TT> ISession</TT> passed.
	 */
	void sessionCreated(ISession session);

	/**
	 * Called when a session started.
	 *
	 * @param	session	The session that is starting.
	 *
	 * @return	<TT>true</TT> if plugin is applicable to passed
	 *			session else <TT>false</TT>.
	 */
	boolean sessionStarted(ISession session);

	/**
	 * Called when a session shutdown.
	 */
	void sessionEnding(ISession session);

	/**
	 * Create panels for the Session Properties dialog.
	 *
	 * @param	session	The session that will be displayed in the properties dialog.
	 *
	 * @return	Array of <TT>ISessionPropertiesPanel</TT> objects. Return
	 *			empty array of <TT>null</TT> if this plugin doesn't require
	 *			any panels in the Session Properties Dialog.
	 */
	ISessionPropertiesPanel[] getSessionPropertiesPanels(ISession session);

	/**
	 * Let app know what extra types of objects in object tree that
	 * plugin can handle.
	 */
	IPluginDatabaseObjectType[] getObjectTypes(ISession session);
}

