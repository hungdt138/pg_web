package com.nms.iwebservice;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.crm.util.StringUtil;

public class ServiceRequest
{
	private long	partnerId;
	private long	agentId;
	private String	password;

	private long	requestId;
	private String	requestDate;

	

	private String	description;

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	

	public long getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(long partnerId) {
		this.partnerId = partnerId;
	}

	public long getAgentId()
	{
		return agentId;
	}

	public void setAgentId(long agentId)
	{
		this.agentId = agentId;
	}

	public long getrequestId()
	{
		return requestId;
	}

	public void setrequestId(long requestId)
	{
		this.requestId = requestId;
	}

	public String getrequestDate()
	{
		return requestDate;
	}

	public void setrequestDate(String requestDate)
	{
		this.requestDate = requestDate;
	}

	
	

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String toString()
	{
		Class<? extends ServiceRequest> type = this.getClass();
		Method[] methods = type.getMethods();
		String returnString = "";
		for (int i = 0; i < methods.length; i++)
		{
			if (!methods[i].getName().startsWith("get"))
			{
				continue;
			}
			String member = "";
			try
			{
				member = methods[i].getName().substring(3) + "=";
				Object value = methods[i].invoke(this, new Object[] {});
				if (value instanceof Date || value instanceof Calendar)
				{
					member += (new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")).format(value);
				}
				else
				{
					member += value.toString();
				}
				member += " | ";
			}
			catch (Exception e)
			{
				member = "";
			}
			returnString += member;
		}
		return returnString.trim();
	}

}
