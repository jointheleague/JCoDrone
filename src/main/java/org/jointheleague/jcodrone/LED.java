package org.jointheleague.jcodrone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jointheleague.jcodrone.protocol.DataType;
import org.jointheleague.jcodrone.protocol.Header;
import org.jointheleague.jcodrone.protocol.Serializable;
import org.jointheleague.jcodrone.protocol.common.Command;
import org.jointheleague.jcodrone.protocol.light.*;

public class LED {
    private static Logger log = LogManager.getLogger(LED.class);

    public static void setMode(CoDrone coDrone, LightMode mode, boolean defaultColor) {
        DataType dataType = DataType.fromClass(mode.getClass());
        if (defaultColor) {
            if (dataType == DataType.LIGHT_MODE_COLOR) {
                dataType = DataType.LIGHT_MODE_DEFAULT_COLOR;
            } else {
                throw new IllegalArgumentException("Default color must be set using RGB.");
            }
        }
        Header header = new Header(dataType, mode.getInstanceSize());

        coDrone.transfer(header, mode);
    }

    public static void setMode2(CoDrone coDrone, LightMode mode1, LightMode mode2, boolean defaultColor) {
        Header header;
        DataType dataType;
        Serializable message;

        if (mode1.getClass().equals(mode2.getClass())) {
            switch (mode1.getClass().getName()) {
                case "LightModeColors":
                    message = new LightModeColors2((LightModeColors) mode1, (LightModeColors) mode2);
                    dataType = DataType.LIGHT_MODE_2;
                    break;
                case "LightModeColor":
                    message = new LightModeColor2((LightModeColor) mode1, (LightModeColor) mode2);
                    if (defaultColor) {
                        dataType = DataType.LIGHT_MODE_DEFAULT_COLOR_2;
                    } else {
                        dataType = DataType.LIGHT_MODE_COLOR_2;
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized light mode");
            }
        } else {
            throw new IllegalArgumentException("Both light modes must use the same class of color.");
        }

        header = new Header(dataType, message.getInstanceSize());
        coDrone.transfer(header, message);
    }

    public static void setModeCommand(CoDrone coDrone, LightModeColors mode, CommandType commandType, byte option) {
        Header header = new Header(DataType.LIGHT_MODE_COMMAND, LightModeCommand.getSize());
        Command command = new Command(commandType, option);
        LightModeCommand lightModeCommand = new LightModeCommand(mode, command);

        coDrone.transfer(header, lightModeCommand);
    }

    public static void setLightModeCommandIR(CoDrone coDrone, LightModeColors mode, CommandType commandType, byte option, short irData) {
        Header header = new Header(DataType.LIGHT_MODE_COMMAND_IR, LightModeCommandIR.getSize());
        Command command = new Command(commandType, option);
        LightModeCommandIR lightModeCommandIr = new LightModeCommandIR(mode, command, irData);

        coDrone.transfer(header, lightModeCommandIr);
    }

    public static void setLightEvent(CoDrone coDrone, LightEvent event) {
        Header header = new Header(DataType.fromClass(event.getClass()), event.getInstanceSize());
        coDrone.transfer(header, event);
    }

    public static void setLightEventCommand(CoDrone coDrone, LightEventColors event, CommandType commandType, byte option) {
        Header header = new Header(DataType.LIGHT_EVENT_COMMAND, LightEventCommand.getSize());
        Command command = new Command(commandType, option);

        LightEventCommand lightEventCommand = new LightEventCommand(event, command);

        coDrone.transfer(header, lightEventCommand);
    }

    public static void setLightEventCommandIR(CoDrone coDrone, LightEventColors event, CommandType commandType, byte option, short irData) {
        Header header = new Header(DataType.LIGHT_EVENT_COMMAND_IR, LightEventCommandIR.getSize());
        Command command = new Command(commandType, option);

        LightEventCommandIR lightEventCommandIR = new LightEventCommandIR(event, command, irData);

        coDrone.transfer(header, lightEventCommandIR);
    }
}
