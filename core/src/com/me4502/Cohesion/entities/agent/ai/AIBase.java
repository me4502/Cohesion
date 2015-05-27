package com.me4502.Cohesion.entities.agent.ai;

import com.me4502.Cohesion.entities.agent.Agent;

public abstract class AIBase {

	AIStatus status;

	public final Agent agent;

	public AIBase(Agent agent) {
		this.agent = agent;
	}

	public boolean isBlocking() {
		return getStatus() != AIStatus.STALLING;
	}

	public AIStatus getStatus() {
		return status == null ? AIStatus.STALLING : status;
	}

	public void setStatus(AIStatus status) {
		this.status = status;
	}

	public void update() {

		if(getStatus() == AIStatus.STALLING) {
			stall();
		} else if(getStatus() == AIStatus.SEARCHING) {
			search();
		} else if(getStatus() == AIStatus.WORKING) {
			work();
		} else if(getStatus() == AIStatus.DONE) {
			done();
			setStatus(AIStatus.STALLING);
		}
	}

	public abstract void stall();

	public abstract void search();

	public abstract void work();

	public abstract void done();

	public enum AIStatus {
		STALLING,
		SEARCHING,
		WORKING,
		DONE
	}
}
