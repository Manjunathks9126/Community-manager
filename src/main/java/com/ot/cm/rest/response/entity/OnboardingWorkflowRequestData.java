package com.ot.cm.rest.response.entity;

import java.util.Date;

public class OnboardingWorkflowRequestData {

        private Long dataId;
        private Long requestId;
		private Long taskId;
		private String status;
		private String outputData;
		private Date createdTimestamp;
		private String modifiedBy;
		private Date modifiedTimestamp;
		
		public Long getDataId() {
			return dataId;
		}
		public void setDataId(Long dataId) {
			this.dataId = dataId;
		}
		public Long getRequestId() {
			return requestId;
		}
		public void setRequestId(Long requestId) {
			this.requestId = requestId;
		}
		public Long getTaskId() {
			return taskId;
		}
		public void setTaskId(Long taskId) {
			this.taskId = taskId;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public String getOutputData() {
			return outputData;
		}
		public void setOutputData(String outputData) {
			this.outputData = outputData;
		}
		public Date getCreatedTimestamp() {
			return createdTimestamp;
		}
		public void setCreatedTimestamp(Date createdTimestamp) {
			this.createdTimestamp = createdTimestamp;
		}
		public String getModifiedBy() {
			return modifiedBy;
		}
		public void setModifiedBy(String modifiedBy) {
			this.modifiedBy = modifiedBy;
		}
		public Date getModifiedTimestamp() {
			return modifiedTimestamp;
		}
		public void setModifiedTimestamp(Date modifiedTimestamp) {
			this.modifiedTimestamp = modifiedTimestamp;
		}
		
		@Override
		public String toString() {
			return "OnboardingWorkflowRequestData [dataId=" + dataId + ", requestId=" + requestId + ", taskId=" + taskId
					+ ", status=" + status + ", outputData=" + outputData + ", createdTimestamp=" + createdTimestamp
					+ ", modifiedBy=" + modifiedBy + ", modifiedTimestamp=" + modifiedTimestamp + "]";
		}
  
}
