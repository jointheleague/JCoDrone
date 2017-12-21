package org.jointheleague.jcodrone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jointheleague.jcodrone.protocol.CommandType;
import org.jointheleague.jcodrone.protocol.DataType;
import org.jointheleague.jcodrone.protocol.Header;
import org.jointheleague.jcodrone.protocol.Serializable;
import org.jointheleague.jcodrone.protocol.common.Command;
import org.jointheleague.jcodrone.protocol.light.*;

public class LED {
    private static Logger log = LogManager.getLogger(LED.class);

    private final CoDrone coDrone;

    public LED(CoDrone coDrone) {
        this.coDrone = coDrone;
    }

    public void setMode(LightMode mode) {
        Header header = new Header(DataType.fromClass(mode.getClass()), mode.getInstanceSize());

        coDrone.transfer(header, mode);
    }

    public void setMode2(LightMode mode1, LightMode mode2) {
        setMode2(mode1, mode2, false);
    }

    private void setMode2(LightMode mode1, LightMode mode2, boolean defaultColor) {
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

    public void setLightModeDefaultColor(LightModeColor mode) {
        Header header = new Header(DataType.LIGHT_MODE_DEFAULT_COLOR, LightModeDefaultColor.getSize());
        coDrone.transfer(header, mode);
    }

    public void setLightModeDefaultColor2(LightModeColor mode1, LightModeColor mode2) {
        setMode2(mode1, mode2, true);
    }

    public void setModeCommand(LightModeColors mode, CommandType commandType, byte option) {
        Header header = new Header(DataType.LIGHT_MODE_COMMAND, LightModeCommand.getSize());
        Command command = new Command(commandType, option);
        LightModeCommand lightModeCommand = new LightModeCommand(mode, command);

        coDrone.transfer(header, lightModeCommand);
    }

    public void setLightModeCommandIr(LightModeColors mode, CommandType commandType, byte option, short irData) {
        Header header = new Header(DataType.LIGHT_MODE_COMMAND_IR, LightModeCommandIR.getSize());
        Command command = new Command(commandType, option);
        LightModeCommandIR lightModeCommandIr = new LightModeCommandIR(mode, command, irData);

        coDrone.transfer(header, lightModeCommandIr);
    }

    public void setLightEvent(LightEvent event) {
        Header header = new Header(DataType.fromClass(event.getClass()), event.getInstanceSize());
        coDrone.transfer(header, event);
    }

    public void setLightEventCommand(LightEventColors event, CommandType commandType, byte option) {
        Header header = new Header(DataType.LIGHT_EVENT_COMMAND, LightEventCommand.getSize());
        Command command = new Command(commandType, option);

        LightEventCommand lightEventCommand = new LightEventCommand(event, command);

        coDrone.transfer(header, lightEventCommand);
    }

    public void setLightEventCommandIr(LightEventColors event, CommandType commandType, byte option, short irData) {
        Header header = new Header(DataType.LIGHT_EVENT_COMMAND_IR, LightEventCommandIR.getSize());
        Command command = new Command(commandType, option);

        LightEventCommandIR lightEventCommandIR = new LightEventCommandIR(event, command, irData);

        coDrone.transfer(header, lightEventCommandIR);
    }
}
