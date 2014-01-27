package com.glu.glutest.res;

import com.glu.glutest.dao.Profile;
import com.glu.glutest.dao.ProfileDAO;
import com.glu.glutest.transport.BusinessReq;
import com.glu.glutest.transport.BusinessResp;
import com.google.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/ping")
public class PingRes {
    private static final Logger log = LoggerFactory.getLogger(PingRes.class);

    private final ProfileDAO profileDAO;

    @Inject
    public PingRes(ProfileDAO profileDAO) {
        this.profileDAO = profileDAO;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(BusinessResp.JSON_CONTENT_UTF8)
    public BusinessResp ping(BusinessReq<Object> req) {
        log.info("ping");
        String userId = req.getUserId();
        if (StringUtils.isEmpty(userId)) {
            BusinessResp resp = new BusinessResp();
            resp.setCode(BusinessResp.INVALID_REQUEST);
            resp.setMessage("Missing request param (userId)");
            return resp;
        }

        Profile profile = profileDAO.get(userId);
        if (profile == null) {
            profile = new Profile();
            profile.setId(req.getUserId());
            profile.setPings(0);
        }
        profile.setPings(profile.getPings() + 1);
        profileDAO.save(profile);

        BusinessResp resp = new BusinessResp();
        resp.setCode(BusinessResp.OK);
        resp.setPayload("PONG " + profile.getPings());
        return resp;
    }
}
