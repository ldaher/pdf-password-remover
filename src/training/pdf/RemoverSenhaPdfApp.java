package training.pdf;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;

import training.pdf.enums.FileOptions;

public class RemoverSenhaPdfApp {

	private static final String TITULO_ERRO = "Erro";
	private static final String ARQUIVO_INVALIDO = "O arquivo fornecido não é uma arquivo PDF válido ou está corrompido.";
	private static final String TITULO_AVISO = "Aviso";
	private static final String ARQUIVO_NAO_CRIPTO = "O arquivo PDF fornecido não está encriptado por senha.";
	private static final String SENHA_INCORRETA = "Senha informada não é valida. Tente novamente";

	public static void main(String[] args) {
		File readPdf = openSavePdfFile(FileOptions.OPEN);
		try (PDDocument pdDocument = PDDocument.load(readPdf)) {
			JOptionPane.showMessageDialog(null, ARQUIVO_NAO_CRIPTO, TITULO_AVISO, JOptionPane.WARNING_MESSAGE);
		} catch (IOException ex) {
			if (ex instanceof InvalidPasswordException) {
				while (true) {
					String senha = passwordInputPanel();
					try (PDDocument pdDocument = PDDocument.load(readPdf, senha);) {
						pdDocument.setAllSecurityToBeRemoved(true);
						File file = openSavePdfFile(FileOptions.SAVE);
						pdDocument.save(file);
						System.exit(0);
					} catch (IOException ey) {
						JOptionPane.showMessageDialog(null, SENHA_INCORRETA, TITULO_AVISO, JOptionPane.WARNING_MESSAGE);
					}
				}
			} else {
				JOptionPane.showMessageDialog(null, ARQUIVO_INVALIDO, TITULO_ERRO, JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private static File openSavePdfFile(FileOptions options) {
		JFileChooser fileChooser = new JFileChooser();

		Integer status = 0;
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

		if (status.equals(JFileChooser.CANCEL_OPTION)) {
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
