package net.sourceforge.squirrel_sql.plugins.sqlval;
/*
 * Copyright (C) 2002 Colin Bell
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
import java.awt.event.ActionEvent;

import javax.swing.JDialog;

import net.sourceforge.squirrel_sql.fw.util.ICommand;
import net.sourceforge.squirrel_sql.fw.util.IMessageHandler;
import net.sourceforge.squirrel_sql.fw.util.Resources;
import net.sourceforge.squirrel_sql.fw.util.log.ILogger;
import net.sourceforge.squirrel_sql.fw.util.log.LoggerController;

import net.sourceforge.squirrel_sql.client.IApplication;
import net.sourceforge.squirrel_sql.client.action.SquirrelAction;
import net.sourceforge.squirrel_sql.client.session.ISQLPanelAPI;
import net.sourceforge.squirrel_sql.client.session.ISession;
import net.sourceforge.squirrel_sql.client.session.SQLPanelAPI;
import net.sourceforge.squirrel_sql.client.session.action.ISessionAction;

/**
 * This action will validate the current SQL.
 *
 * @author  <A HREF="mailto:colbell@users.sourceforge.net">Colin Bell</A>
 */
public class ValidateSQLAction extends SquirrelAction implements ISessionAction
{
	/** Logger for this class. */
	private final static ILogger s_log =
		LoggerController.createLogger(ValidateSQLAction.class);

	/** Preferences. */
	private final WebServicePreferences _prefs;

	/** Current plugin. */
	private final SQLValidatorPlugin _plugin;

	/** Current session. */
	private ISession _session;

	/**
	 * Ctor.
	 * 
	 * @param	app		Application API.
	 * @param	rsrc	Resources to build this action from.
	 * @param	prefs	Plugin preferences.
	 * @param	plugin	Plugin
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown if <TT>null</TT>WebServicePreferences</TT> passed.
	 */
	public ValidateSQLAction(IApplication app, Resources rsrc,
									WebServicePreferences prefs,
									SQLValidatorPlugin plugin)
	{
		super(app, rsrc);
		if (prefs == null)
		{
			throw new IllegalArgumentException("WebServicePreferences == null");
		}
		_prefs = prefs;
		_plugin = plugin;
	}

	public void actionPerformed(ActionEvent evt)
	{
		if (_session != null)
		{
			final WebServiceSessionProperties props = _plugin.getSessionProperties(_session);
			final IApplication app = getApplication();
			final ISQLPanelAPI api = _session.getSQLPanelAPI(_plugin);
			final String sql = api.getSQLScriptToBeExecuted();
			if (sql != null && sql.trim().length() > 0)
			{
				final char stmtSepChar = _session.getProperties().getSQLStatementSeparatorChar();
				final ValidationProps valProps = new ValidationProps(_prefs, props,
														_session.getMessageHandler(),
														sql, stmtSepChar);
				final Executor executor = new Executor(app, valProps);
				if (_prefs.getShowConfirmationDialog())
				{
					JDialog dlog = new ValidationDialog(app, valProps, executor);
					dlog.setVisible(true);
				}
				else
				{
					executor.execute();
				}
			}
			else
			{
				_session.getMessageHandler().showErrorMessage("No SQL specified");
			}
		}
	}

	public void setSession(ISession session)
	{
		_session = session;
	}

	static final class ValidationProps
	{
		final WebServicePreferences _prefs;
		final WebServiceSessionProperties _sessionProps;
		final IMessageHandler _msgHandler;
		final String _sql;
		final char _stmtSepChar;

		ValidationProps(WebServicePreferences prefs,
						WebServiceSessionProperties sessionProps,
						IMessageHandler msgHandler, String sql, char stmtSepChar)
		{
			super();
			_prefs = prefs;
			_sessionProps = sessionProps;
			_msgHandler = msgHandler;
			_sql = sql;
			_stmtSepChar = stmtSepChar;
		}
	}

	static class Executor implements ICommand
	{
		private final IApplication _app;
		private final ValidationProps _valProps;

		Executor(IApplication app, ValidationProps valProps)
		{
			super();
			_app = app;
			_valProps = valProps;
		}

		public void execute()
		{
			ValidateSQLCommand cmd = new ValidateSQLCommand(_valProps._prefs,
											_valProps._sessionProps,
											_valProps._sql, _valProps._stmtSepChar);
			try
			{
				cmd.execute();
				_valProps._msgHandler.showMessage(cmd.getResults());
			}
			catch (Throwable th)
			{
				final String msg = "Error occured when talking to the web service";
				s_log.error(msg, th);
				_app.showErrorDialog(msg, th);
			}
		}
	}
}
