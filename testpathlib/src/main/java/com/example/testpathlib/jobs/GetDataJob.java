package com.example.testpathlib.jobs;

import com.example.testpathlib.Utils;
import com.example.testpathlib.events.Event;
import com.path.android.jobqueue.BaseJob;

import java.util.concurrent.atomic.AtomicInteger;

import de.greenrobot.event.EventBus;

/**
 * Created by Julien Quiévreux on 23/12/13.
 */
public class GetDataJob extends BaseJob
{

    private static final AtomicInteger jobCounter = new AtomicInteger(0);

    private static boolean requiresNetwork = false;

    private static boolean persistent = false;

    private static String groupId = "get-data";

    private final int id;

    public GetDataJob()
    {
        super(requiresNetwork, persistent, groupId);
        id = jobCounter.incrementAndGet();
    }

    @Override
    public void onAdded()
    {

    }

    @Override
    public void onRun() throws Throwable
    {
        /**
         * regarde si d'autre requête similaire on été ajouté après celle-ci.
         * Il n'y a pas de raison de télécharger de multiple fois les données,
         * donc si c'est le cas on coupe le traitement doublon
         */
        if (id != jobCounter.get())
        {
            return;
        }
        Thread.sleep(2*1000);//simulate work
        String result= Utils.GetHTML("www.google.fr", null);
        EventBus.getDefault().post(new Event.TextDataReceived(result));
    }

    @Override
    protected void onCancel()
    {
        EventBus.getDefault().post(new Event.TextDataReceived("Action stoppé"));
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable)
    {
        EventBus.getDefault().post(new Event.ErrorTextDataReceived("internet absent"));
        return false;
    }


}
