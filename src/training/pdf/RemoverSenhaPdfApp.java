package training.pdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.exceptions.BadPasswordException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

import training.pdf.enums.FileOptions;

public class RemoverSenhaPdfApp {

	private static final String TITULO_ARQUIVO_INVALIDO = "Alerta";
	private static final String ARQUIVO_INVALIDO = "O arquivo fornecido não é uma arquivo PDF válido ou está corrompido.";
	private static final String TITULO_ARQUIVO_NAO_CRIPTO = "Erro";
	private static final String ARQUIVO_NAO_CRIPTO = "O arquivo PDF fornecido não está seguro por senha.";

	public static void main(String[] args) {
		try {

			File readPdf = openSavePdfFile(FileOptions.OPEN);

			PdfReader.unethicalreading = true;
			PdfReader pdfReader;

			String senha = passwordInputPanel();

			pdfReader = new PdfReader(new FileInputStream(readPdf), senha.getBytes());

			File file = openSavePdfFile(FileOptions.SAVE);
			PdfStamper stamper = new PdfStamper(pdfReader, new FileOutputStream(file));
			stamper.setEncryption(false, null, null, PdfWriter.ALLOW_PRINTING);
			stamper.close();
			pdfReader.close();

		} catch (IOException | DocumentException e) {
			if (e instanceof BadPasswordException) {
				JOptionPane.showMessageDialog(null, ARQUIVO_NAO_CRIPTO, TITULO_ARQUIVO_NAO_CRIPTO,
						JOptionPane.WARNING_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null,
						ARQUIVO_INVALIDO, TITULO_ARQUIVO_INVALIDO,
						JOptionPane.ERROR_MESSAGE);
			}
			
			e.printStackTrace();
		}
	}

	private static File openSavePdfFile(FileOptions options) {
		JFileChooser fileChooser = new JFileChooser();

		int status = 0;
		switch (options.getValue()) {
		case 0:
			fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			status = fileChooser.showOpenDialog(null);
			break;
		case 1:
			fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
			status = fileChooser.showSaveDialog(null);
			break;
		default:
			break;
		}

		switch (status) {
		case JFileChooser.CANCEL_OPTION:
			System.exit(JFileChooser.CANCEL_OPTION);
		}

		return fileChooser.getSelectedFile();
	}

	private static String passwordInputPanel() {
		JPanel p = new JPanel();
		JLabel l = new JLabel("Senha do arquivo: ");
		JPasswordField jpf = new JPasswordField(10);
		p.add(l);
		p.add(jpf);

		String[] options = { "Ok", "Cancelar" };
		int optionResult = JOptionPane.showOptionDialog(null, p, "Senha", JOptionPane.NO_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

		if (optionResult == 0) {
			char[] password = jpf.getPassword();
			return new String(password);
		}

		return null;
	}
}
