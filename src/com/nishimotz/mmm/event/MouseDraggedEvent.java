/*
 * �쐬��: 2005/12/24
 *
 * TODO ���̐������ꂽ�t�@�C���̃e���v���[�g��ύX����ɂ͎��փW�����v:
 * �E�B���h�E - �ݒ� - Java - �R�[�h�E�X�^�C�� - �R�[�h�E�e���v���[�g
 */
package com.nishimotz.mmm.event;

import java.awt.event.MouseEvent;


public class MouseDraggedEvent extends CastStudioEvent {

	public MouseDraggedEvent(MouseEvent arg0) {
		setMouseEvent(arg0);
	}

}
