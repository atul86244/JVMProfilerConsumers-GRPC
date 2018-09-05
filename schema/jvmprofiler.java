public class Jvmprofiler
{
    private String host;

    private null appId;

    private String nonHeapMemoryTotalUsed;

    private String heapMemoryTotalUsed;

    private String systemCpuLoad;

    private Gc[] gc;

    private String heapMemoryCommitted;

    private String vmRSS;

    private String nonHeapMemoryCommitted;

    private String epochMillis;

    private String name;

    private MemoryPools[] memoryPools;

    private String processCpuTime;

    private String processCpuLoad;

    private String processUuid;

    private String vmHWM;

    private BufferPools[] bufferPools;

    public String getHost ()
    {
        return host;
    }

    public void setHost (String host)
    {
        this.host = host;
    }

    public null getAppId ()
    {
        return appId;
    }

    public void setAppId (null appId)
    {
        this.appId = appId;
    }

    public String getNonHeapMemoryTotalUsed ()
    {
        return nonHeapMemoryTotalUsed;
    }

    public void setNonHeapMemoryTotalUsed (String nonHeapMemoryTotalUsed)
    {
        this.nonHeapMemoryTotalUsed = nonHeapMemoryTotalUsed;
    }

    public String getHeapMemoryTotalUsed ()
    {
        return heapMemoryTotalUsed;
    }

    public void setHeapMemoryTotalUsed (String heapMemoryTotalUsed)
    {
        this.heapMemoryTotalUsed = heapMemoryTotalUsed;
    }

    public String getSystemCpuLoad ()
    {
        return systemCpuLoad;
    }

    public void setSystemCpuLoad (String systemCpuLoad)
    {
        this.systemCpuLoad = systemCpuLoad;
    }

    public Gc[] getGc ()
    {
        return gc;
    }

    public void setGc (Gc[] gc)
    {
        this.gc = gc;
    }

    public String getHeapMemoryCommitted ()
    {
        return heapMemoryCommitted;
    }

    public void setHeapMemoryCommitted (String heapMemoryCommitted)
    {
        this.heapMemoryCommitted = heapMemoryCommitted;
    }

    public String getVmRSS ()
    {
        return vmRSS;
    }

    public void setVmRSS (String vmRSS)
    {
        this.vmRSS = vmRSS;
    }

    public String getNonHeapMemoryCommitted ()
    {
        return nonHeapMemoryCommitted;
    }

    public void setNonHeapMemoryCommitted (String nonHeapMemoryCommitted)
    {
        this.nonHeapMemoryCommitted = nonHeapMemoryCommitted;
    }

    public String getEpochMillis ()
    {
        return epochMillis;
    }

    public void setEpochMillis (String epochMillis)
    {
        this.epochMillis = epochMillis;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public MemoryPools[] getMemoryPools ()
    {
        return memoryPools;
    }

    public void setMemoryPools (MemoryPools[] memoryPools)
    {
        this.memoryPools = memoryPools;
    }

    public String getProcessCpuTime ()
    {
        return processCpuTime;
    }

    public void setProcessCpuTime (String processCpuTime)
    {
        this.processCpuTime = processCpuTime;
    }

    public String getProcessCpuLoad ()
    {
        return processCpuLoad;
    }

    public void setProcessCpuLoad (String processCpuLoad)
    {
        this.processCpuLoad = processCpuLoad;
    }

    public String getProcessUuid ()
    {
        return processUuid;
    }

    public void setProcessUuid (String processUuid)
    {
        this.processUuid = processUuid;
    }

    public String getVmHWM ()
    {
        return vmHWM;
    }

    public void setVmHWM (String vmHWM)
    {
        this.vmHWM = vmHWM;
    }

    public BufferPools[] getBufferPools ()
    {
        return bufferPools;
    }

    public void setBufferPools (BufferPools[] bufferPools)
    {
        this.bufferPools = bufferPools;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [host = "+host+", appId = "+appId+", nonHeapMemoryTotalUsed = "+nonHeapMemoryTotalUsed+", heapMemoryTotalUsed = "+heapMemoryTotalUsed+", systemCpuLoad = "+systemCpuLoad+", gc = "+gc+", heapMemoryCommitted = "+heapMemoryCommitted+", vmRSS = "+vmRSS+", nonHeapMemoryCommitted = "+nonHeapMemoryCommitted+", epochMillis = "+epochMillis+", name = "+name+", memoryPools = "+memoryPools+", processCpuTime = "+processCpuTime+", processCpuLoad = "+processCpuLoad+", processUuid = "+processUuid+", vmHWM = "+vmHWM+", bufferPools = "+bufferPools+"]";
    }
}