/**
 * Copyright (C) 2013 Loophole, LLC
 * <p>
 * This program is free software: you can redistribute it and/or  modify
 * it under the terms of the GNU Affero General Public License, version 3,
 * as published by the Free Software Foundation.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * As a special exception, the copyright holders give permission to link the
 * code of portions of this program with the OpenSSL library under certain
 * conditions as described in each individual source file and distribute
 * linked combinations including the program with the OpenSSL library. You
 * must comply with the GNU Affero General Public License in all respects for
 * all of the code used other than as permitted herein. If you modify file(s)
 * with this exception, you may extend this exception to your version of the
 * file(s), but you are not obligated to do so. If you do not wish to do so,
 * delete this exception statement from your version. If you delete this
 * exception statement from all source files in the program, then also delete
 * it in the license file.
 */
package com.baiyi.caesar.facade.jenkins.task;

import com.alibaba.fastjson.JSON;
import com.baiyi.caesar.facade.jenkins.impl.EngineFacadeImpl;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.Session;

/**
 * class to send output to web socket client
 */
@Slf4j
public class SentEngineChartTask implements Runnable {

    Session session;
    String sessionId;

    private static final long SLEEP = 10000L;

    public SentEngineChartTask(String sessionId, Session session) {
        this.sessionId = sessionId;
        this.session = session;
    }

    @Override
    public void run() {
        while (session.isOpen()) {
            try {
                if (EngineFacadeImpl.chart != null) {
                    session.getBasicRemote().sendText(JSON.toJSONString(EngineFacadeImpl.chart));
                }
                Thread.sleep(SLEEP);
            } catch (Exception ex) {
                log.error(ex.toString(), ex);
            }
        }
    }
}
