import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.security.auth.DestroyFailedException;
import javax.security.auth.Destroyable;
import java.util.*;

/**
 * 使用PULL解析器解释Project.xml
 */
public class ProjectReader implements Destroyable {
    private boolean isAvaliable;
    private XmlPullParserFactory xmlFactory;

    private File m_file;
    private FileInputStream m_stream;

    private GameInfo gameInfo;

    public ProjectReader(File file) {
        isAvaliable = true;
        m_file = file;
    }

    public boolean load(World.Option opt) {
        try {
            m_stream = new FileInputStream(m_file);

            xmlFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = xmlFactory.newPullParser();
            parser.setInput(m_stream, "UTF-8");
            int event = parser.getEventType();
            Stack<String> fatherNodes = new Stack<>();
			gameInfo = new GameInfo();
            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("Values")) {
                            fatherNodes.push(parser.getAttributeValue(0));
							System.out.println(fatherNodes.peek());
                        }
						
						if(fatherNodes.isEmpty()) {
							break;
						}
                        if ("GameInfo".equals(fatherNodes.peek())) {
                            gameInfo.setValue(parser);
                        }else if ("Player".equals(fatherNodes.peek()) && "SpawnPosition".equals(parser.getAttributeValue(0))) {
                            String[] str = parser.getAttributeValue(2).split(",");
                            opt.origin = new Point(((int) Float.parseFloat(str[0])) / 16, ((int) Float.parseFloat(str[2])) / 16);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("Values")) {
                            fatherNodes.pop();
                        }
                        break;
                }
                event = parser.next();
            }
        } catch (Exception e) {
            isAvaliable = false;
            e.printStackTrace();
        }
		System.out.println("project load finished");
        return isAvaliable;
    }

    public File save() {
        return m_file;
    }

    @Override
    public void destroy() throws DestroyFailedException {
        try {
            m_stream.close();
            isAvaliable = false;
        } catch (IOException e) {
            throw new DestroyFailedException(e.getMessage());
        }
    }

    @Override
    public boolean isDestroyed() {
        return isAvaliable;
    }

    public class GameInfo {
        public int WorldSeed;
        public String WorldName;
        public String TerrainGenerationMode;
        public int TerrainLevel;
        public int TerrainBlockIndex;
        public int TerrainOceanBlockIndex;
        public int TemperatureOffset;
        public int HumidityOffset;
        public int SeaLevelOffset;

        public void setValue(XmlPullParser parser) {
            if ("WorldSeed".equals(parser.getAttributeValue(0))) {
                WorldSeed = Integer.parseInt(parser.getAttributeValue(2));
            }else if ("WorldName".equals(parser.getAttributeValue(0))) {
                WorldName = parser.getAttributeValue(2);
            }else if ("TerrainGenerationMode".equals(parser.getAttributeValue(0))) {
                TerrainGenerationMode = parser.getAttributeValue(2);
            }else if ("TerrainLevel".equals(parser.getAttributeValue(0))) {
                TerrainLevel = Integer.parseInt(parser.getAttributeValue(2));
            }else if ("TerrainBlockIndex".equals(parser.getAttributeValue(0))) {
                TerrainBlockIndex = Integer.parseInt(parser.getAttributeValue(2));
            }else if ("TerrainOceanBlockIndex".equals(parser.getAttributeValue(0))) {
                TerrainOceanBlockIndex = Integer.parseInt(parser.getAttributeValue(2));
            }else if ("TemperatureOffset".equals(parser.getAttributeValue(0))) {
                TemperatureOffset = Integer.parseInt(parser.getAttributeValue(2));
            }else if ("HumidityOffset".equals(parser.getAttributeValue(0))) {
                HumidityOffset = Integer.parseInt(parser.getAttributeValue(2));
            }else if ("SeaLevelOffset".equals(parser.getAttributeValue(0))) {
                SeaLevelOffset = Integer.parseInt(parser.getAttributeValue(2));
            }
        }
    }
}
