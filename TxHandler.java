/*  Benjamin Paspaporn - University Of Manitoba
*   2019-2020
*   
*
*
*
*/

import java.security.PublicKey;
import java.util.ArrayList;


public class TxHandler {

    /**
     * Creates a public ledger whose current UTXOPool (collection of unspent transaction outputs) is
     * {@code utxoPool}. This should make a copy of utxoPool by using the UTXOPool(UTXOPool uPool)
     * constructor.
     */
    public TxHandler(UTXOPool utxoPool) {
      /*
        We need to create a public ledger in which unspent transactions (our UTXOPool)
        can be gathered.
      */

        // first declare our transaction pool
        private UTXOPool utxoPool;
        // and secondly construct our pool
        public TxHandler(UTXOPool pool) {
          utxoPool = new UTXOPool(utxoPool);
        }



    /**
     * @return true if:
     * (1) all outputs claimed by {@code tx} are in the current UTXO pool,
     * (2) the signatures on each input of {@code tx} are valid,
     * (3) no UTXO is claimed multiple times by {@code tx},
     * (4) all of {@code tx}s output values are non-negative, and
     * (5) the sum of {@code tx}s input values is greater than or equal to the sum of its output
     *     values; and false otherwise.
     */
    public boolean isValidTx(Transaction tx) {
        // consider the properties above that will satisfy the conditions
        // our coin
        boolean result = true;
        double totalInput = 0;
        double totalOutput = 0;

        // we will iterate through the our inputs and verify that
        // our conditions are satisfied
        for (int i = 0; i < tx.numInputs();i++) {
          Transaction.Input input = tx.getInput(i);
          UTXO utxo = new UTXO(input.prevTxHash, input.outputIndex);
          Transaction.Output output = this.utxoPool.getTxOutput(utxo);

          if (prevOutput == null)
            result = false;

          PublicKey pk = prevOutput.address;
          byte[] msg - tx.getRawDataToSign(i);
          byte[] sig = input.sig;

          if (!Crypto.verifySignature(pk, msg, sig))
            result = false;

          if (checkPool.contains(utxo))
            result = false;

          checkPool.addUTXO(utxo, prevOutput);
          totalInput += prevOutput.value;
        }

        for (int i = 0; i < tx.numOutputs(); i++) {
          Transaction.Output currOutput = tx.getOutput(i);

          if (currOutput.value < 0)
            result = false;

          totalOutput += currOutput.value;
        }

        result = (inputTotal >= outputTotal);

        return result;

    }

    /**
     * Handles each epoch by receiving an unordered array of proposed transactions, checking each
     * transaction for correctness, returning a mutually valid array of accepted transactions, and
     * updating the current UTXO pool as appropriate.
     */
    public Transaction[] handleTxs(Transaction[] possibleTxs) {
        ArrayList<Transaction> result = new ArrayList<Transaction>();

        for (Transaction tx : possibleTxs) {

          if (!this.isValidTx(tx))
            continue;

          result.add(tx);

          for (Transaction.Input input : tx.getInputs()) {
            UTXO utxo = new UTXO(input.prevTxHash, input.outputIndex);
            this.utxoPool.removeUTXO(utxo);
          }

          for (int i = 0; i < tx.numOutputs(); i++) {
            Transaction.Output output = tx.getOutput(i);
            UTXO utxo = new UTXO(tx.getHash(), i);
            this.utxoPool.addUTXO(utxo, output);
          }
        }

        Transaction[] newResult = new Transaction[result.size()];
        return result.toArray(newResult);
        }
    }

}
