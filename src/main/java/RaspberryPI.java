/**
 * Created by tamirov on 25.02.2017.
 */

import com.pi4j.gpio.extension.base.AdcGpioProvider;
import com.pi4j.gpio.extension.mcp.MCP3008GpioProvider;
import com.pi4j.gpio.extension.mcp.MCP3008Pin;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinAnalogInput;
import com.pi4j.io.spi.SpiChannel;
import com.pi4j.io.spi.SpiDevice;

public class RaspberryPI {
    public static void main(String[] args) {

        System.out.println("<--Pi4J--> MCP3008 ADC Example (NON-MONITORED) ... started.");

        // Create gpio controller
        final GpioController gpio = GpioFactory.getInstance();

        // Create custom MCP3008 analog gpio provider
        // we must specify which chip select (CS) that that ADC chip is physically connected to.
        final AdcGpioProvider provider = new MCP3008GpioProvider(SpiChannel.CS0,
                SpiDevice.DEFAULT_SPI_SPEED,
                SpiDevice.DEFAULT_SPI_MODE,
                false);   // <<-- the 'false' value here disable the base background monitoring thread

        // So why would I want to disable the background monitoring thread?
        // Well, that depends on how you plan on integrating this into your project.
        // If you need/want pin event notification, then you must keep the background
        // monitoring thread enabled.  If you only need to periodically obtain analog
        // input conversion values or only need to acquire the value as the result of
        // some other event or condition in your application, then you can disable the
        // background monitoring thread to reduce the runtime overhead.
        // If its disabled, then anytime you request the pin.getValue() to get an analog
        // conversion value it will get the value directly from the ADC chip synchronously
        // in your process call.  If background monitoring is enabled, then calls to
        // pin.getValue() return you the last acquired (cached) value and does not
        // perform an immediate data acquisition.

        // Provision gpio analog input pins for all channels of the MCP3008.
        // (you don't have to define them all if you only use a subset in your project)
        final GpioPinAnalogInput inputs[] = {
                gpio.provisionAnalogInputPin(provider, MCP3008Pin.CH0, "MyAnalogInput-CH0"),
                gpio.provisionAnalogInputPin(provider, MCP3008Pin.CH1, "MyAnalogInput-CH1"),
                gpio.provisionAnalogInputPin(provider, MCP3008Pin.CH2, "MyAnalogInput-CH2"),
                gpio.provisionAnalogInputPin(provider, MCP3008Pin.CH3, "MyAnalogInput-CH3"),
                gpio.provisionAnalogInputPin(provider, MCP3008Pin.CH4, "MyAnalogInput-CH4"),
                gpio.provisionAnalogInputPin(provider, MCP3008Pin.CH5, "MyAnalogInput-CH5"),
                gpio.provisionAnalogInputPin(provider, MCP3008Pin.CH6, "MyAnalogInput-CH6"),
                gpio.provisionAnalogInputPin(provider, MCP3008Pin.CH7, "MyAnalogInput-CH7")
        };

        // Keep this sample program running for 10 minutes
        for (int count = 0; count < 600; count++) {
            StringBuilder sb  = new StringBuilder();

            // Print current analog input conversion values from each input channel
            for(GpioPinAnalogInput input : inputs){
                sb.append(" \t[" + input.getValue() + "] ");
            }

            // Print out all analog input conversion values
            System.out.println("<MCP3008 VALUES> " + sb.toString());

            Thread.sleep(1000);
        }

        // When your program is finished, make sure to stop all GPIO activity/threads by shutting
        // down the GPIO controller (this method will forcefully shutdown all GPIO monitoring threads
        // and background scheduled tasks)
        gpio.shutdown();

        System.out.println("Exiting MCP3008GpioExampleNonMonitored");
    }
}
