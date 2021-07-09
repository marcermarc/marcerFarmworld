package de.marcermarc.farmworld.timer;

import com.cronutils.model.Cron;
import com.cronutils.model.time.ExecutionTime;
import de.marcermarc.farmworld.controller.PluginController;
import de.marcermarc.farmworld.models.WorldSettings;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Optional;

public class RecreateTimer implements Runnable {
    private static final int TICKS_PER_MINUTE = 20 * 60;

    private final PluginController controller;

    private int taskId;

    public RecreateTimer(PluginController controller) {
        this.controller = controller;
    }

    public void init() {
        taskId = controller.getMain().getServer().getScheduler()
                .runTaskTimerAsynchronously(controller.getMain(), this, 0, TICKS_PER_MINUTE)
                .getTaskId();
    }

    @Override
    public void run() {
        for (Map.Entry<String, WorldSettings> entry : controller.getConfig().getWorldSettings().entrySet()) {
            Cron rule = entry.getValue().getCronRule();

            if (rule != null) {
                ExecutionTime execTime = ExecutionTime.forCron(rule);

                Optional<ZonedDateTime> nextForThisWorld = execTime.nextExecution(entry.getValue().getLastRecreation());

                if (nextForThisWorld.isPresent() && nextForThisWorld.get().isBefore(ZonedDateTime.now())) {
                    controller.getMain().getServer().getScheduler().runTask(controller.getMain(),
                        () -> controller.getWorldController().recreate(entry.getKey(), null)
                    );
                }
            }
        }
    }
}
