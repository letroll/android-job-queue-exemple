package com.example.testpathlib.events;

/**
 * Created by Julien Quiévreux on 23/12/13.
 */
public class Event
{
    public static class TextDataReceived{
        private String data;

        public TextDataReceived(String data) {this.data = data;}

        public String getData()
        {
            return data;
        }
    };

    public static class ErrorTextDataReceived extends TextDataReceived{

        public ErrorTextDataReceived(String data)
        {
            super(data);
        }
    };
}
