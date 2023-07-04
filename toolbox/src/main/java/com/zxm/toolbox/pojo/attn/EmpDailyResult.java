package com.zxm.toolbox.pojo.attn;

import java.time.LocalDateTime;
import java.util.List;

public class EmpDailyResult {
	
	private int clockInType;
	private int clockInDuration;
	private int clockOutType;
	private int clockOutDuration;
	private String post;
	private String businessHour;
	private LocalDateTime clockInTime;
	private LocalDateTime clockOutTime;
	private List<LocalDateTime> punchingCardRecord;

	// 每个结果用三位数字表示
	// 每天上下班
	// 提前
	public static final byte ADVANCED = 1;
	// 延迟
	public static final byte DELAYED = 2;
	// 正常
	public static final byte NORMAL = 3;
	// 不需要
	public static final byte UNNEED_CLOCK = 4;
	// 需要
	public static final byte NEED_CLOCK = 5;
	// 无效
	public static final byte INVALID = 6;
	// 提前异常
	public static final byte ADVANCED_EXCEPTION = 7;
	// 延迟异常
	public static final byte DELAYED_EXCEPTION = 8;
	//不须打卡但有打卡记录
	public static final byte UNNEED_BUT_HAVE = 9;
	// 异常
	public static final byte EXCEPTION = 127;

	public int getClockInType() {
		return clockInType;
	}

	public void setClockInType(int clockInType) {
		this.clockInType = clockInType;
	}

	public int getClockInDuration() {
		return clockInDuration;
	}

	public void setClockInDuration(int clockInDuration) {
		this.clockInDuration = clockInDuration;
	}

	public int getClockOutType() {
		return clockOutType;
	}

	public void setClockOutType(int clockOutType) {
		this.clockOutType = clockOutType;
	}

	public int getClockOutDuration() {
		return clockOutDuration;
	}

	public void setClockOutDuration(int clockOutDuration) {
		this.clockOutDuration = clockOutDuration;
	}

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public String getBusinessHour() {
		return businessHour;
	}

	public void setBusinessHour(String businessHour) {
		this.businessHour = businessHour;
	}

	public LocalDateTime getClockInTime() {
		return clockInTime;
	}

	public void setClockInTime(LocalDateTime clockInTime) {
		this.clockInTime = clockInTime;
	}

	public LocalDateTime getClockOutTime() {
		return clockOutTime;
	}

	public void setClockOutTime(LocalDateTime clockOutTime) {
		this.clockOutTime = clockOutTime;
	}

	public List<LocalDateTime> getPunchingCardRecord() {
		return punchingCardRecord;
	}

	public void setPunchingCardRecord(List<LocalDateTime> punchingCardRecord) {
		this.punchingCardRecord = punchingCardRecord;
	}

	public String punchingRecord2String() {
		StringBuffer tempSb = new StringBuffer("");
		if (punchingCardRecord != null) {
			for (LocalDateTime record : punchingCardRecord) {
				tempSb.append(record);
				tempSb.append(";");
			}
		}
		return tempSb.toString();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((businessHour == null) ? 0 : businessHour.hashCode());
		result = prime * result + clockInDuration;
		result = prime * result + ((clockInTime == null) ? 0 : clockInTime.hashCode());
		result = prime * result + clockInType;
		result = prime * result + clockOutDuration;
		result = prime * result + ((clockOutTime == null) ? 0 : clockOutTime.hashCode());
		result = prime * result + clockOutType;
		result = prime * result + ((post == null) ? 0 : post.hashCode());
		result = prime * result + ((punchingCardRecord == null) ? 0 : punchingCardRecord.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EmpDailyResult other = (EmpDailyResult) obj;
		if (businessHour == null) {
			if (other.businessHour != null)
				return false;
		} else if (!businessHour.equals(other.businessHour))
			return false;
		if (clockInDuration != other.clockInDuration)
			return false;
		if (clockInTime == null) {
			if (other.clockInTime != null)
				return false;
		} else if (!clockInTime.equals(other.clockInTime))
			return false;
		if (clockInType != other.clockInType)
			return false;
		if (clockOutDuration != other.clockOutDuration)
			return false;
		if (clockOutTime == null) {
			if (other.clockOutTime != null)
				return false;
		} else if (!clockOutTime.equals(other.clockOutTime))
			return false;
		if (clockOutType != other.clockOutType)
			return false;
		if (post == null) {
			if (other.post != null)
				return false;
		} else if (!post.equals(other.post))
			return false;
		if (punchingCardRecord == null) {
			if (other.punchingCardRecord != null)
				return false;
		} else if (!punchingCardRecord.equals(other.punchingCardRecord))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "EmpDailyResult2 [clockInType=" + clockInType + ", clockInDuration=" + clockInDuration + ", clockOutType="
				+ clockOutType + ", clockOutDuration=" + clockOutDuration + ", post=" + post + ", businessHour="
				+ businessHour + ", punchingCardRecord=" + punchingRecord2String() + "]";
	}


}
