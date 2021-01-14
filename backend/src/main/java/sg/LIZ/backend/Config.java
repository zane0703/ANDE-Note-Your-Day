package sg.LIZ.backend;

import java.util.Properties;

import org.glassfish.jersey.internal.guava.MoreObjects.ToStringHelper;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvBuilder;

public class Config {
	public static final String DB_HOSTNAME;
	public static final Properties DB_PROPERTIES =new Properties();
	public static final String JWT_SECRET ;
	static {
		Dotenv dotenv=  new DotenvBuilder().ignoreIfMissing().load();
		DB_HOSTNAME = new StringBuilder("jdbc:mysql://localhost/note_your_day")
				.append(dotenv.get("DB_HOSTNAME"))
				.append('/')
				.append(dotenv.get("note_your_day"))
				.append( new char[] {'?', 's', 'e', 'r', 'v', 'e', 'r', 'T', 'i', 'm', 'e', 'z', 'o', 'n', 'e', '=', 'U', 'T', 'C'})
				.toString();
		DB_PROPERTIES.put("user", dotenv.get("DB_USERNAME"));
		DB_PROPERTIES.put("password", dotenv.get("DB_PASSWORD"));
		JWT_SECRET = dotenv.get("JWT_SECRET");
	}

}
