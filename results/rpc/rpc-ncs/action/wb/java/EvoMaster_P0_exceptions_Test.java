import  org.junit.AfterClass;
import  org.junit.BeforeClass;
import  org.junit.Before;
import  org.junit.Test;
import static org.junit.Assert.*;
import  java.util.Map;
import  java.util.Arrays;
import  java.util.List;
import static org.evomaster.test.utils.EMTestUtils.*;
import  org.evomaster.client.java.controller.SutHandler;
import static org.hamcrest.Matchers.*;
import static org.evomaster.client.java.controller.contentMatchers.NumberMatcher.*;
import static org.evomaster.client.java.controller.contentMatchers.StringMatcher.*;
import static org.evomaster.client.java.controller.contentMatchers.SubStringMatcher.*;




/**
*  This file was automatically generated by EvoMaster on 2024-11-06T22:34:06.772-03:00[America/Argentina/Buenos_Aires]
 * <br>
*  The generated test suite contains 5 tests
 * <br>
*  Covered targets: 10
 * <br>
*  Used time: 0h 11m 22s
 * <br>
*  Needed budget for current results: 96%
 * <br>
*  This file contains test cases which throws exceptions.
*/
public class EvoMaster_P0_exceptions_Test {

    
    private static final SutHandler controller = new em.external.org.rpc.grpcncs.ExternalEvoMasterController("/run/datad/facultad/tesis/EMB/jdk_8_maven/cs/rpc/grpc/artificial/grpc-ncs/target/rpc-grpc-ncs-sut.jar");
    private static String baseUrlOfSut;
    private static org.grpc.ncs.generated.NcsServiceGrpc.NcsServiceBlockingStub var_client0_NcsServiceGrpc_NcsServiceBlockingStub;
    
    
    @BeforeClass
    public static void initClass() {
        controller.setupForGeneratedTest();
        baseUrlOfSut = controller.startSut();
        controller.registerOrExecuteInitSqlCommandsIfNeeded();
        controller.extractRPCSchema();
        assertNotNull(baseUrlOfSut);
        var_client0_NcsServiceGrpc_NcsServiceBlockingStub = (org.grpc.ncs.generated.NcsServiceGrpc.NcsServiceBlockingStub) controller.getRPCClient("org.grpc.ncs.generated.NcsServiceGrpc$NcsServiceBlockingStub");
    }
    
    
    @AfterClass
    public static void tearDown() {
        controller.stopSut();
    }
    
    
    @Before
    public void initTest() {
        controller.resetStateOfSUT();
    }
    
    
    
    
    @Test(timeout = 60000)
    public void test_0_ncsServiceBlockingStubOnRemainderThrowsStatusRuntimeException() throws Exception {
        
        try{
            
            org.grpc.ncs.generated.DtoResponse res_0 = null;
            {
             org.grpc.ncs.generated.RemainderRequest arg0 = null;
             {
              org.grpc.ncs.generated.RemainderRequest.Builder arg0builder = org.grpc.ncs.generated.RemainderRequest.newBuilder();
              arg0builder.setA(391);
              arg0builder.setB(524787);
              arg0 = arg0builder.build();
             }
             res_0 = var_client0_NcsServiceGrpc_NcsServiceBlockingStub.remainder(arg0);
            }// org/grpc/ncs/generated/NcsServiceGrpc$MethodHandlers_576_invoke UNEXPECTED_EXCEPTION:io.grpc.StatusRuntimeException
            
        } catch(Exception e){
            // INVALID_ARGUMENT
        }
    }
    
    
    @Test(timeout = 60000)
    public void test_1_ncsServiceBlockingStubOnBessjThrowsStatusRuntimeException() throws Exception {
        
        try{
            
            org.grpc.ncs.generated.DtoResponse res_0 = null;
            {
             org.grpc.ncs.generated.BessjRequest arg0 = null;
             res_0 = var_client0_NcsServiceGrpc_NcsServiceBlockingStub.bessj(arg0);
            }// org/grpc/ncs/generated/NcsServiceGrpc$MethodHandlers_576_invoke UNEXPECTED_EXCEPTION:io.grpc.StatusRuntimeException
            
        } catch(Exception e){
            // INVALID_ARGUMENT
        }
    }
    
    
    @Test(timeout = 60000)
    public void test_2_ncsServiceBlockingStubOnGammqThrowsStatusRuntimeException() throws Exception {
        
        try{
            
            org.grpc.ncs.generated.DtoResponse res_0 = null;
            {
             org.grpc.ncs.generated.GammqRequest arg0 = null;
             res_0 = var_client0_NcsServiceGrpc_NcsServiceBlockingStub.gammq(arg0);
            }// org/grpc/ncs/imp/Gammq_80_exe UNEXPECTED_EXCEPTION:io.grpc.StatusRuntimeException
            
        } catch(Exception e){
            // UNKNOWN
        }
    }
    
    
    @Test(timeout = 60000)
    public void test_3_ncsServiceBlockingStubOnFisherThrowsStatusRuntimeException() throws Exception {
        
        try{
            
            org.grpc.ncs.generated.DtoResponse res_0 = null;
            {
             org.grpc.ncs.generated.FisherRequest arg0 = null;
             {
              org.grpc.ncs.generated.FisherRequest.Builder arg0builder = org.grpc.ncs.generated.FisherRequest.newBuilder();
              arg0builder.setM(1642759004);
              arg0builder.setN(674);
              arg0builder.setX(0.5958913949580318);
              arg0 = arg0builder.build();
             }
             res_0 = var_client0_NcsServiceGrpc_NcsServiceBlockingStub.fisher(arg0);
            }// org/grpc/ncs/generated/NcsServiceGrpc$MethodHandlers_576_invoke UNEXPECTED_EXCEPTION:io.grpc.StatusRuntimeException
            
        } catch(Exception e){
            // INVALID_ARGUMENT
        }
    }
    
    
    @Test(timeout = 60000)
    public void test_4_ncsServiceBlockingStubOnExpintThrowsStatusRuntimeException() throws Exception {
        
        try{
            
            org.grpc.ncs.generated.DtoResponse res_0 = null;
            {
             org.grpc.ncs.generated.ExpintRequest arg0 = null;
             res_0 = var_client0_NcsServiceGrpc_NcsServiceBlockingStub.expint(arg0);
            }// org/grpc/ncs/imp/Expint_16_exe UNEXPECTED_EXCEPTION:io.grpc.StatusRuntimeException
            
        } catch(Exception e){
            // UNKNOWN
        }
    }


}
