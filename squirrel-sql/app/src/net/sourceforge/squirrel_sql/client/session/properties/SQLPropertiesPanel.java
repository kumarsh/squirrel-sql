package net.sourceforge.squirrel_sql.client.session.properties;
/*
 * Copyright (C) 2001-2002 Colin Bell
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
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.sourceforge.squirrel_sql.fw.gui.CharField;
import net.sourceforge.squirrel_sql.fw.gui.FontChooser;
import net.sourceforge.squirrel_sql.fw.gui.FontInfo;
import net.sourceforge.squirrel_sql.fw.gui.IntegerField;
import net.sourceforge.squirrel_sql.fw.gui.MultipleLineLabel;

import net.sourceforge.squirrel_sql.client.IApplication;
import net.sourceforge.squirrel_sql.client.preferences.INewSessionPropertiesPanel;
import net.sourceforge.squirrel_sql.client.preferences.SquirrelPreferences;
import net.sourceforge.squirrel_sql.client.resources.SquirrelResources;
import net.sourceforge.squirrel_sql.client.session.ISession;

public class SQLPropertiesPanel
		implements INewSessionPropertiesPanel, ISessionPropertiesPanel {

	private IApplication _app;
	private SessionProperties _props;

	private MyPanel _myPanel;

	public SQLPropertiesPanel(IApplication app)
			throws IllegalArgumentException {
		super();
		if (app == null) {
			throw new IllegalArgumentException("Null IApplication passed");
		}
		_app = app;
		_myPanel = new MyPanel(app);
	}

	public void initialize(IApplication app) {
		_props = app.getSquirrelPreferences().getSessionProperties();
		_myPanel.loadData(_props);
	}

	public void initialize(IApplication app, ISession session)
			throws IllegalArgumentException {
		if (session == null) {
			throw new IllegalArgumentException("Null ISession passed");
		}
		_props = session.getProperties();
		_myPanel.loadData(_props);
	}

	public Component getPanelComponent() {
		return _myPanel;
	}

	public String getTitle() {
		return MyPanel.i18n.SQL;
	}

	public String getHint() {
		return MyPanel.i18n.SQL;
	}

	public void applyChanges() {
		_myPanel.applyChanges(_props);
	}

	private static final class MyPanel extends JPanel {
		/**
		 * This interface defines locale specific strings. This should be
		 * replaced with a property file.
		 */
		interface i18n {
			String AUTO_COMMIT = "Auto Commit SQL";
			String COMMIT_ON_CLOSE = "Commit On Closing Session";
			String NBR_ROWS_CONTENTS = "Number of rows:";
			String NBR_ROWS_SQL = "Number of rows:";
			String LIMIT_ROWS_CONTENTS = "Contents - Limit rows";
			String LIMIT_ROWS_SQL = "SQL - Limit rows";
			String SHOW_ROW_COUNT = "Show Row Count for Tables";
			String TABLE = "Table";
			String TEXT = "Text";
			String STATEMENT_SEPARATOR = "Statement Separator:";
			String PERF_WARNING = "Note: Settings marked with an hourglass will have performance implications.";
			String SQL = "SQL";
		}

		private JCheckBox _autoCommitChk = new JCheckBox(i18n.AUTO_COMMIT);
		private JCheckBox _commitOnClose = new JCheckBox(i18n.COMMIT_ON_CLOSE);
		private IntegerField _contentsNbrRowsToShowField = new IntegerField();
		private JCheckBox _contentsLimitRowsChk = new JCheckBox(i18n.LIMIT_ROWS_CONTENTS);
		private JCheckBox _showRowCount = new JCheckBox(i18n.SHOW_ROW_COUNT);
		private IntegerField _sqlNbrRowsToShowField = new IntegerField();
		private JCheckBox _sqlLimitRows = new JCheckBox(i18n.LIMIT_ROWS_SQL);
		private CharField _stmtSepChar = new CharField();

		/** Label displaying the selected font. */
		private JLabel _fontLbl = new JLabel();

		/** Button to select font. */
		private FontButton _fontBtn = new FontButton("Font", _fontLbl);

		MyPanel(IApplication app) {
			super();
			createUserInterface(app);
		}

		void loadData(SessionProperties props) {
			_autoCommitChk.setSelected(props.getAutoCommit());
			_commitOnClose.setSelected(props.getCommitOnClosingConnection());
			_contentsNbrRowsToShowField.setInt(props.getContentsNbrRowsToShow());
			_contentsLimitRowsChk.setSelected(props.getContentsLimitRows());
			_sqlNbrRowsToShowField.setInt(props.getSQLNbrRowsToShow());
			_sqlLimitRows.setSelected(props.getSQLLimitRows());
			_showRowCount.setSelected(props.getShowRowCount());
			_stmtSepChar.setChar(props.getSQLStatementSeparatorChar());

			FontInfo fi = props.getFontInfo();
			if (fi == null) {
				fi = new FontInfo(UIManager.getFont("TextArea.font"));
			}
			_fontLbl.setText(fi.toString());
			_fontBtn.setSelectedFont(fi.createFont());
		}

		void applyChanges(SessionProperties props) {
			props.setAutoCommit(_autoCommitChk.isSelected());
			props.setCommitOnClosingConnection(_commitOnClose.isSelected());
			props.setContentsNbrRowsToShow(_contentsNbrRowsToShowField.getInt());
			props.setContentsLimitRows(_contentsLimitRowsChk.isSelected());
			props.setSQLNbrRowsToShow(_sqlNbrRowsToShowField.getInt());
			props.setSQLLimitRows(_sqlLimitRows.isSelected());
			props.setShowRowCount(_showRowCount.isSelected());
			props.setSQLStatementSeparatorChar(_stmtSepChar.getChar());
			props.setFontInfo(_fontBtn.getFontInfo());
		}

		private void createUserInterface(IApplication app) {
			setLayout(new GridBagLayout());
			final GridBagConstraints gbc = new GridBagConstraints();
			gbc.anchor = gbc.WEST;
			gbc.fill = gbc.HORIZONTAL;
			gbc.insets = new Insets(4, 4, 4, 4);

			gbc.gridx = 0;
			gbc.gridy = 0;
			add(createSQLPanel(app), gbc);

			++gbc.gridy;
			add(createFontPanel(), gbc);

			gbc.gridx = 0;
			++gbc.gridy;
			gbc.gridwidth = GridBagConstraints.REMAINDER;
			add(new MultipleLineLabel(i18n.PERF_WARNING), gbc);
		}

		private JPanel createSQLPanel(IApplication app) {
			JPanel pnl = new JPanel();
			pnl.setBorder(BorderFactory.createTitledBorder("SQL"));
			pnl.setLayout(new GridBagLayout());
			final GridBagConstraints gbc = new GridBagConstraints();
			gbc.fill = gbc.HORIZONTAL;
			gbc.insets = new Insets(4, 4, 4, 4);

			final Icon warnIcon = app.getResources().getIcon(SquirrelResources.IImageNames.PERFORMANCE_WARNING);

			_autoCommitChk.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent evt) {
					_commitOnClose.setEnabled(!((JCheckBox)evt.getSource()).isSelected());
				}
			});

			_contentsLimitRowsChk.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent evt) {
					_contentsNbrRowsToShowField.setEnabled(_contentsLimitRowsChk.isSelected());
				}
			});

			_sqlLimitRows.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent evt) {
					_sqlNbrRowsToShowField.setEnabled(_sqlLimitRows.isSelected());
				}
			});

			_contentsNbrRowsToShowField.setColumns(5);
			_sqlNbrRowsToShowField.setColumns(5);
			_stmtSepChar.setColumns(1);

			gbc.gridx = 0;
			gbc.gridy = 0;
			pnl.add(_autoCommitChk, gbc);
			++gbc.gridy;
			pnl.add(_showRowCount, gbc);
			++gbc.gridy;
			pnl.add(_contentsLimitRowsChk, gbc);
			++gbc.gridy;
			pnl.add(_sqlLimitRows, gbc);

			gbc.insets = new Insets(2, 0, 2, 4);
			++gbc.gridx;
			gbc.gridy = 1;
			pnl.add(new JLabel(warnIcon), gbc);
			++gbc.gridy;
			pnl.add(new JLabel(warnIcon), gbc);
			++gbc.gridy;
			pnl.add(new JLabel(warnIcon), gbc);

			gbc.insets = new Insets(2, 0, 2, 4);
			++gbc.gridx;
			gbc.gridy = 0;
			gbc.gridwidth = 2;
			pnl.add(_commitOnClose, gbc);
			++gbc.gridy;
			gbc.gridwidth = 1;
			++gbc.gridy;
			pnl.add(new RightLabel(i18n.NBR_ROWS_CONTENTS), gbc);
			++gbc.gridy;
			pnl.add(new RightLabel(i18n.NBR_ROWS_SQL), gbc);
			++gbc.gridy;
			pnl.add(new RightLabel(i18n.STATEMENT_SEPARATOR), gbc);

			gbc.insets = new Insets(2, 0, 2, 1);
			++gbc.gridx;
			gbc.gridy = 0;
			++gbc.gridy;
			++gbc.gridy;
			pnl.add(_contentsNbrRowsToShowField, gbc);
			++gbc.gridy;
			pnl.add(_sqlNbrRowsToShowField, gbc);
			++gbc.gridy;
			pnl.add(_stmtSepChar, gbc);

			++gbc.gridx;
			gbc.gridy = 2;
			pnl.add(new JLabel(warnIcon), gbc);
			++gbc.gridy;
			pnl.add(new JLabel(warnIcon), gbc);

			return pnl;
		}

		private JPanel createFontPanel() {
			JPanel pnl = new JPanel();
			pnl.setBorder(BorderFactory.createTitledBorder("SQL Entry Area"));
			pnl.setLayout(new GridBagLayout());
			final GridBagConstraints gbc = new GridBagConstraints();
			gbc.fill = gbc.HORIZONTAL;
			gbc.insets = new Insets(4, 4, 4, 4);

			_fontBtn.addActionListener(new FontButtonListener());

			gbc.gridx = 0;
			gbc.gridy = 0;
			pnl.add(_fontBtn, gbc);

			++gbc.gridx;
			gbc.fill = gbc.HORIZONTAL;
			gbc.weightx = 1.0;
			pnl.add(_fontLbl, gbc);

			return pnl;
		}

		private static final class RightLabel extends JLabel {
			RightLabel(String title) {
				super(title, SwingConstants.RIGHT);
			}
		}

		private static final class FontButton extends JButton {
			private FontInfo _fi;
			private JLabel _lbl;
			private Font _font;
//			private boolean _dirty;

			FontButton(String text, JLabel lbl) {
				super(text);
				_lbl = lbl;
			}

			FontInfo getFontInfo() {
				return _fi;
			}

			Font getSelectedFont() {
				return _font;
			}

			void setSelectedFont(Font font) {
				_font = font;
				if (_fi == null) {
					_fi = new FontInfo(font);
				} else {
					_fi.setFont(font);
				}
//				_dirty = true;
			}

//			boolean isDirty() {
//				return _dirty;
//			}
		}

		private static final class FontButtonListener implements ActionListener {
			public void actionPerformed(ActionEvent evt) {
				if (evt.getSource() instanceof FontButton) {
					FontButton btn = (FontButton)evt.getSource();
					FontInfo fi = btn.getFontInfo();
					Font font = null;
					if (fi != null) {
						font = fi.createFont();
					}
					font = new FontChooser().showDialog(font);
					if (font != null) {
						btn.setSelectedFont(font);
						btn._lbl.setText(new FontInfo(font).toString());
					}
				}
			}
		}

		private final static class OutputType {
			private final String _name;
			private final String _className;

			OutputType(String name, String className) {
				super();
				_name = name;
				_className = className;
			}

			public String toString() {
				return _name;
			}

			String getPanelClassName() {
				return _className;
			}
		}

	}
}
