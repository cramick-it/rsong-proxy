package coop.rchain.repo

import coop.rchain.casper.protocol.{DeployData, DeployServiceGrpc}
import coop.rchain.model.{Err, ErrorCode}
import io.grpc.ManagedChannelBuilder


object DeployRholangGrpc {
  def apply(host: String, port: Int): DeployRholangGrpc =
    new DeployRholangGrpc(host, port)
}

class DeployRholangGrpc(host: String, port: Int) {
  private lazy val channel =
    ManagedChannelBuilder.forAddress(host, port).usePlaintext(true).build

  def deployContract(contract: String) = {
     val resp =  DeployServiceGrpc.blockingStub(channel).doDeploy(
      DeployData().withTerm(contract))
       if (resp.success)
         Right(resp.message)
       else Left(Err(ErrorCode.grpcDeploy, resp.message, Some(contract)))
  }
}
