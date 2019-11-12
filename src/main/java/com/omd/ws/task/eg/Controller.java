package com.omd.ws.task.eg;

import com.omd.ws.task.ExecutableTask;
import com.omd.ws.task.Task;
import com.omd.ws.task.TaskContext;
import com.omd.ws.task.TaskService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

@RestController
public class Controller {

    private TaskService taskService;
    private int taskCount = 1;

    public Controller(TaskService taskService) {
        this.taskService = taskService;
    }

    @RequestMapping(value = "/api/start-task", method = RequestMethod.GET)
    public void startATask() {
        taskService.submit(new Task("Arbitrary Task " + taskCount, new ArbitraryTask()));
        taskCount++;
    }

    class ArbitraryTask implements ExecutableTask {

        private Random random = new Random();
        private long endTime;

        @Override
        public void run(TaskContext context) throws Exception {
            //Every job between 30 - 90 seconds
            int runtimeSecs = random.nextInt(59) + 31;
            endTime = System.currentTimeMillis() + (runtimeSecs * 1000);
            double progress = 0d;

            while(System.currentTimeMillis() < endTime) {
                try {
                    Thread.sleep(2000);
                    double progressRemaining = 100d - progress;
                    double progressToAdd = random.nextDouble() / progressRemaining;
                    context.addProgress("Added " + BigDecimal.valueOf(progressToAdd).setScale(2, RoundingMode.HALF_EVEN).toString() + "% to progress", progressToAdd);
                    progress += progressToAdd;
                } catch (InterruptedException e) {
                    //Would produce task error
                    throw new IOException("Task was interrupted while sleeping", e);
                }
            }
        }
    }
}
