package com.zxm.toolbox.exception.attn;

import com.zxm.toolbox.pojo.attn.Post;

public class DuplicatePostException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3093491638083575113L;

	private Post p1;
	private Post p2;

	public DuplicatePostException(Post p1, Post p2) {
		super();
		this.p1 = p1;
		this.p2 = p2;
	}

	@Override
	public String getMessage() {
		return "岗位名冲突:\n"
				+ p1 + "\n"
				+ p2;
	}
}
