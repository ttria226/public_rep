package com.xsrw.job.util;

import org.quartz.JobExecutionContext;

import com.xsrw.job.domain.SysJob;

/**
 * 定时任务处理（允许并发执行）
 * 
 * @author zjj
 *
 */
public class QuartzJobExecution extends AbstractQuartzJob
{
    @Override
    protected void doExecute(JobExecutionContext context, SysJob sysJob) throws Exception
    {
        JobInvokeUtil.invokeMethod(sysJob);
    }
}
