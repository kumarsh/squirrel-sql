package net.sourceforge.squirrel_sql.client.plugin.api;
/*
 * Copyright (C) 2002 Colin Bell
 * colbell@users.sourceforge.net
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
import net.sourceforge.squirrel_sql.client.session.ISession;
import net.sourceforge.squirrel_sql.client.session.mainpanel.objecttree.INodeExpander;
/**
 * This class is the API through which plugins can work with the object tree.
 *
 * @author  <A HREF="mailto:colbell@users.sourceforge.net">Colin Bell</A>
 */
class ObjectTreeAPI implements IObjectTreeAPI
{
	/** Session containing the object tree. */
	private ISession _session;

	/**
	 * Ctor specifying the session.
	 * 
	 * @param	session	<TT>ISession</TT> containing the object tree.
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown if <T>null</TT> <TT>ISession</TT> passed.
	 */
	ObjectTreeAPI(ISession session)
	{
		super();
		if (session == null)
		{
			throw new IllegalArgumentException("ISession == null");
		}

		_session = session;
	}

	/**
	 * Register an expander for the specified database object type.
	 * 
	 * @param	dbObjectType	Databae object type.
	 *							@see net.sourceforge.squirrel_sql.fw.sql.IDatabaseObjectTypes
	 * @param	expander		Expander called to add children to a parent node.
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown if a <TT>null</TT> <TT>INodeExpander</TT> thrown.
	 */
	public void registerExpander(int dbObjectType, INodeExpander expander)
	{
	}
}
