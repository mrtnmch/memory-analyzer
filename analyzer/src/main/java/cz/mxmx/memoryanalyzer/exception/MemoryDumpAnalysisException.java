package cz.mxmx.memoryanalyzer.exception;

public class MemoryDumpAnalysisException extends Exception {
    public MemoryDumpAnalysisException() {
    }

    public MemoryDumpAnalysisException(String message) {
        super(message);
    }

    public MemoryDumpAnalysisException(String message, Throwable cause) {
        super(message, cause);
    }

    public MemoryDumpAnalysisException(Throwable cause) {
        super(cause);
    }

    public MemoryDumpAnalysisException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
