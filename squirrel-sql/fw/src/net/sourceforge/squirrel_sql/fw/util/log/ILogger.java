package net.sourceforge.squirrel_sql.fw.util.log;
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

/**
 * This interface describes a logging object.
 *
 * @author  <A HREF="mailto:colbell@users.sourceforge.net">Colin Bell</A>
 */
public interface ILogger {
	public void debug(Object message);
	public void debug(Object message, Throwable th);
	public void info(Object message);
	public void info(Object message, Throwable th);
	public void warn(Object message);
	public void warn(Object message, Throwable th);
	public void error(Object message);
	public void error(Object message, Throwable th);
	
	boolean isDebugEnabled();
	boolean isInfoEnabled();

	void setPriority(LoggingLevel level);
}

