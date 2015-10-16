/*
* The contents of this file are subject to the terms of the Common Development and
* Distribution License (the License). You may not use this file except in compliance with the
* License.
*
* You can obtain a copy of the License at legal/CDDLv1.0.txt. See the License for the
* specific language governing permission and limitations under the License.
*
* When distributing Covered Software, include this CDDL Header Notice in each file and include
* the License file at legal/CDDLv1.0.txt. If applicable, add the following below the CDDL
* Header, with the fields enclosed by brackets [] replaced by your own identifying
* information: "Portions copyright [year] [name of copyright owner]".
*
* Copyright 2015 ForgeRock AS.
*/
package org.forgerock.openam.audit;

import org.forgerock.audit.events.AuditEvent;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public class ActivityAuditorTest {

    ActivityAuditor auditor;

    AuditEventPublisher auditEventPublisher;
    AuditEventFactory auditEventFactory;
    AMActivityAuditEventBuilder builder = mock(AMActivityAuditEventBuilder.class);
    AuditEvent auditEvent = mock(AuditEvent.class);

    @BeforeMethod
    public void setUp() throws Exception {
        auditEventPublisher = mock(AuditEventPublisher.class);
        auditEventFactory = mock(AuditEventFactory.class);

        auditor = new ActivityAuditor(auditEventPublisher, auditEventFactory);
    }

    @Test
    public void shouldGiveNewActivityEventBuilder() throws Exception {
        when(auditor.activityEvent()).thenReturn(builder);

        AMActivityAuditEventBuilder result = auditor.activityEvent();

        assertSame(builder, result, "Expected the same builder to be returned.");
        verify(auditEventFactory, times(1)).activityEvent();
        verifyNoMoreInteractions(auditEventFactory);
        verifyNoMoreInteractions(auditEventPublisher);
    }

    @Test
    public void shouldPublishEvent() throws Exception {
        auditor.publish(auditEvent);

        verify(auditEventPublisher, times(1)).publish(AuditConstants.ACTIVITY_TOPIC, auditEvent);
        verifyNoMoreInteractions(auditEventFactory);
        verifyNoMoreInteractions(auditEventPublisher);
    }

    @Test
    public void shouldReturnTrueForRealmAndTopicAuditing() throws Exception {
        String testRealm = "Any realm";
        String testTopic = "Any topic";

        when(auditEventPublisher.isAuditing(any(String.class), any(String.class))).thenReturn(true);

        boolean result = auditor.isAuditing(testRealm, testTopic);

        assertEquals(true, result);
        verify(auditEventPublisher, times(1)).isAuditing(testRealm, testTopic);
        verifyNoMoreInteractions(auditEventFactory);
        verifyNoMoreInteractions(auditEventPublisher);
    }

    @Test
    public void shouldReturnFalseForRealmAndTopicAuditing() throws Exception {
        String testRealm = "Any realm";
        String testTopic = "Any topic";

        when(auditEventPublisher.isAuditing(any(String.class), any(String.class))).thenReturn(false);

        boolean result = auditor.isAuditing(testRealm, testTopic);

        assertEquals(false, result);
        verify(auditEventPublisher, times(1)).isAuditing(testRealm, testTopic);
        verifyNoMoreInteractions(auditEventFactory);
        verifyNoMoreInteractions(auditEventPublisher);
    }

}
