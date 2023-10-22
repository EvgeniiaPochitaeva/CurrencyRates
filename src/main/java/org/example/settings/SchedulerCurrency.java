package org.example.settings;


import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class SchedulerCurrency {
    public static void Start() throws SchedulerException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        JobDetail jobDetail1 = JobBuilder.newJob(SendMessageByTime.class)
                .withIdentity("myJob1", "group1")
                .build();

        JobDetail jobDetail2 = JobBuilder.newJob(SendMessageByTime.class)
                .withIdentity("myJob2", "group1")
                .build();

        JobDetail jobDetail3 = JobBuilder.newJob(SendMessageByTime.class)
                .withIdentity("myJob3", "group1")
                .build();

        JobDetail jobDetail4 = JobBuilder.newJob(SendMessageByTime.class)
                .withIdentity("myJob4", "group1")
                .build();

        JobDetail jobDetail5 = JobBuilder.newJob(SendMessageByTime.class)
                .withIdentity("myJob5", "group1")
                .build();

        JobDetail jobDetail6 = JobBuilder.newJob(SendMessageByTime.class)
                .withIdentity("myJob6", "group1")
                .build();

        JobDetail jobDetail7 = JobBuilder.newJob(SendMessageByTime.class)
                .withIdentity("myJob7", "group1")
                .build();

        JobDetail jobDetail8 = JobBuilder.newJob(SendMessageByTime.class)
                .withIdentity("myJob8", "group1")
                .build();

        JobDetail jobDetail9 = JobBuilder.newJob(SendMessageByTime.class)
                .withIdentity("myJob9", "group1")
                .build();

        JobDetail jobDetail10 = JobBuilder.newJob(SendMessageByTime.class)
                .withIdentity("myJob10", "group1")
                .build();


        Trigger trigger1 = TriggerBuilder.newTrigger()
                .withIdentity("trigger1", "group1")
                .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(9, 0))
                .build();

        Trigger trigger2 = TriggerBuilder.newTrigger()
                .withIdentity("trigger2", "group1")
                .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(10, 0))
                .build();

        Trigger trigger3 = TriggerBuilder.newTrigger()
                .withIdentity("trigger3", "group1")
                .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(11, 0))
                .build();

        Trigger trigger4 = TriggerBuilder.newTrigger()
                .withIdentity("trigger4", "group1")
                .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(12, 0))
                .build();

        Trigger trigger5 = TriggerBuilder.newTrigger()
                .withIdentity("trigger5", "group1")
                .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(13, 0))
                .build();

        Trigger trigger6 = TriggerBuilder.newTrigger()
                .withIdentity("trigger6", "group1")
                .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(14, 0))
                .build();

        Trigger trigger7 = TriggerBuilder.newTrigger()
                .withIdentity("trigger7", "group1")
                .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(15, 0))
                .build();

        Trigger trigger8 = TriggerBuilder.newTrigger()
                .withIdentity("trigger8", "group1")
                .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(16, 0))
                .build();

         Trigger trigger9 = TriggerBuilder.newTrigger()
                .withIdentity("trigger9", "group1")
                .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(17, 0))
                .build();

         Trigger trigger10 = TriggerBuilder.newTrigger()
                .withIdentity("trigger10", "group1")
                .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(18, 0))
                .build();


        scheduler.scheduleJob(jobDetail1, trigger1);
        scheduler.scheduleJob(jobDetail2, trigger2);
        scheduler.scheduleJob(jobDetail3, trigger3);
        scheduler.scheduleJob(jobDetail4, trigger4);
        scheduler.scheduleJob(jobDetail5, trigger5);
        scheduler.scheduleJob(jobDetail6, trigger6);
        scheduler.scheduleJob(jobDetail7, trigger7);
        scheduler.scheduleJob(jobDetail8, trigger8);
        scheduler.scheduleJob(jobDetail9, trigger9);
        scheduler.scheduleJob(jobDetail10, trigger10);
        scheduler.start();
    }
}