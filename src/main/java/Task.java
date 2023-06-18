import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public final class Task {
    final int machine;
    final int startTime;
    final int stopTime;
    final int duration;
    final int jobNum;
    final int precedenceJob;
    final int id;

    public Task(int id, int startTime, int stopTime, int machine,int duration, int jobNum, int precedenceJob) {
        this.machine = machine;
        this.startTime = startTime;
        this.stopTime = stopTime;
        this.duration = duration;
        this.jobNum = jobNum;
        this.precedenceJob = precedenceJob;
        this.id = id;
    }

    public Task(Task that) {
        this(that.getId(), that.getStartTime(),that.getStopTime(),that.getMachine(),that.getDuration(),that.getJobNum(),that.getPrecedenceJob());
    }

    @Override
    public String toString() {
        return "Task{" +
                "machine=" + machine +
                ", startTime=" + startTime +
                ", stopTime=" + stopTime +
                ", duration=" + duration +
                ", jobNum=" + jobNum +
                ", precedenceJob=" + precedenceJob +
                ", id=" + id +
                '}';
    }
}
