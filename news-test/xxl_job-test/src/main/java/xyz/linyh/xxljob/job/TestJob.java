package xyz.linyh.xxljob.job;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TestJob {

//    @XxlJob("testjobHandler")
//    public void job1(){
//        System.out.println("执行到这个任务了");
//    }

    /**
     * 分片广播测试
     */
    @XxlJob("shardingJobHandler")
    public void job2(){
//        比如一个任务里面有10000个操作，将单数的操作交给其中一个集群，将双数的操作交给另外一个集群

//        当前集群的索引
        int shardIndex = XxlJobHelper.getShardIndex();
//        这个服务全部有多少集群
        int shardTotal = XxlJobHelper.getShardTotal();

        List<Integer> tasks = new ArrayList<>();
        for(int i = 1;i<=100000;i++){
            tasks.add(i);
        }

        for (Integer task : tasks) {
            if(task % shardTotal==shardIndex){
                System.out.println("这个集群:"+shardIndex+",处理了第："+task+"个任务");
            }
        }
    }
}
