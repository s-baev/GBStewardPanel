package com.gillbus.steward.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.gillbus.steward.component.Frame;
import com.gillbus.steward.component.LoginPanel;
import com.gillbus.steward.component.WorkPanel;
import com.gillbus.steward.connection.HttpConnector;
import com.gillbus.steward.connection.ServletURL;

/**
 * 
 * @author Kashpur Artem
 *
 */
public class Login implements ActionListener {

	private HttpConnector httpConnector;
	private LoginPanel loginPanel;
	private WorkPanel workPanel;
	
	
	@Override
	public void actionPerformed(ActionEvent event) {
		try {
			loginPanel = LoginPanel.getInstance();
			httpConnector = HttpConnector.getInstance();
			sendRequest();
			getResponse();
			if (loginPanel.isLogged()) {
				workPanel = WorkPanel.getInstance();
				workPanel.initWorkSpace();
				Frame frame = Frame.getInstance();
				frame.removePanelComponent(loginPanel);
				frame.addPanelComponent(workPanel);
				frame.invalidate();
				frame.validate();
			}
		} catch (IOException e) {
		} 
	}
	
	
	private void getResponse() throws IOException {
		try {
			loginPanel.setUserName(DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().parse(httpConnector.getInputStream())
					.getDocumentElement().getElementsByTagName("NAME").item(0)
					.getFirstChild().getNodeValue());
			if (loginPanel.getUserName() != null && !loginPanel.getUserName().isEmpty()) {
				loginPanel.setLogged(true);
			}
		} catch (SAXException e) {
		} catch (ParserConfigurationException e) {
		} catch (IllegalArgumentException e) {
		} finally {
			httpConnector.releaseMethod();
		}
	}
	
	
	private void sendRequest() throws IOException {
		httpConnector.newMethod(ServletURL.LOGIN);
		httpConnector.addParameter("login", loginPanel.getTextLogin().getText());
		httpConnector.addParameter("password", new String(loginPanel.getTextPswd().getPassword()));
		httpConnector.addParameter("computerId", getComputerId());
		httpConnector.executeMethod();
	}
	
	
	private String getComputerId() {
		String processorId = "";
		try {
			//создаем файл Visual Basic Scripting
			File file = File.createTempFile("getProcessorId", ".vbs");
			file.deleteOnExit();
			FileWriter fw = new java.io.FileWriter(file);
			
			//команда для получения UUID
			String vbs = "strComputer = \".\"" + "\n"
			+ "Set objWMIService = GetObject(\"winmgmts:\" _" + "\n"
			+ " & \"{impersonationLevel=impersonate}!\\\\\" & strComputer & \"\\root\\cimv2\")" + "\n"
			+ "Set colComputer = objWMIService.ExecQuery(\"Select * from Win32_ComputerSystemProduct\")" + "\n"
			+ "For Each objComputer in colComputer" + "\n"
			+ "  WScript.Echo objComputer.UUID" + "\n"
			+ "Next";
			fw.write(vbs);
			fw.close();
			
			//выполняем Visual Basic Scripting
			Process p = Runtime.getRuntime().exec(
					"cscript //NoLogo " + file.getPath());
			
			//считываем полученный результат
			BufferedReader input = new BufferedReader(new InputStreamReader(p
					.getInputStream()));
			String line;
			while ((line = input.readLine()) != null) {
				processorId += line;
			}
			input.close();
		} catch (Exception e) {
		}
		return processorId;
	}

}
