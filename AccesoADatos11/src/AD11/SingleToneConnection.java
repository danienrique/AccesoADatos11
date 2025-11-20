package AD11;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SingleToneConnection {
	Connection conexion ;
	Statement sentencia;
	
	SingleToneConnection() {
		try {
			conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/Alumnos01?useSSL=false&serverTimezone=UTC",
					"ADManager", "manager");
			Statement sentencia = conexion.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
