package com.xxx.chain.ton.bo.toncenter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * <pre>
 *     {
 *   "ok": true,
 *   "result": [
 *     {
 *       "@type": "raw.transaction",
 *       "address": {
 *         "@type": "accountAddress",
 *         "account_address": "EQAWnJNnwByLThRxli2zLpnj5Ec6r7R-GmpkOkF3pnWPFuF-"
 *       },
 *       "utime": 1717314001,
 *       "data": "te6cckECBwEAAZ8AA7Vxack2fAHItOFHGWLbMumePkRzqvtH4aamQ6QXemdY8WAAAqnWO0acG3SpvzwI91duiwNZCcgqmHeC+juP/pSw7yO5DTKwFSegAAKp1jKxWBZlwh0QABRgwY9IAQIDAQGgBACCcgFk5+gIDDB8TMGRGTD3RyFDCmI4t4CTHsy/S9IvGnnf5Bt5Zs+v0n5qUCkKNy6pgmwZOOtui/thNrQsfTNZbfsCFwxCiQDMWHFYYMGOEQUGAMlIAdUSlItN7+LcbqX8mTjv9d8L7k3QJJ4MB3ebwbsvoTmvAAWnJNnwByLThRxli2zLpnj5Ec6r7R+GmpkOkF3pnWPFkAzFhxQGCCNaAABVOsbuwYbMuEN6apk7bao28nfLSC6QwACeQHvsCCx8AAAAAAAAAAAdAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABbwAAAAAAAAAAAAAAAAS1FLaRJ5QuM990nhh8UYSKv4bVGu4tw/IIW8MYUE5+OBHhi76c=",
 *       "transaction_id": {
 *         "@type": "internal.transactionId",
 *         "lt": "46855471000001",
 *         "hash": "jOfjlbJORIMieUfzQN3XrKuP68f63rOGyxYrB54i4z0="
 *       },
 *       "fee": "396410",
 *       "storage_fee": "10",
 *       "other_fee": "396400",
 *       "in_msg": {
 *         "@type": "raw.message",
 *         "source": "EQDqiUpFpvfxbjdS_kycd_rvhfcm6BJPBgO7zeDdl9Cc1yec",
 *         "destination": "EQAWnJNnwByLThRxli2zLpnj5Ec6r7R-GmpkOkF3pnWPFuF-",
 *         "value": "53567941",
 *         "fwd_fee": "266669",
 *         "ihr_fee": "0",
 *         "created_lt": "46855467000003",
 *         "body_hash": "CwL0DGoXxp2IJZ7Y3UfA7l92DKtfEtZgTfr/SMqxikA=",
 *         "msg_data": {
 *           "@type": "msg.dataRaw",
 *           "body": "te6cckEBAQEADgAAGNUydttUbeTvlpBdIdSzqvs=",
 *           "init_state": ""
 *         },
 *         "message": "1TJ221Rt5O+WkF0h\n"
 *       },
 *       "out_msgs": []
 *     },
 *     {
 *       "@type": "raw.transaction",
 *       "address": {
 *         "@type": "accountAddress",
 *         "account_address": "EQAWnJNnwByLThRxli2zLpnj5Ec6r7R-GmpkOkF3pnWPFuF-"
 *       },
 *       "utime": 1717313962,
 *       "data": "te6cckECDAEAArQAA7Vxack2fAHItOFHGWLbMumePkRzqvtH4aamQ6QXemdY8WAAAqnWMrFYG8DQ6NkS5yy4dCkqgA/keKNFtAUfQ2JwINJRuTYuBO/AAAKpUPWTwBZlwhqgADRlCDCoAQIDAgHgBAUAgnJTTuWmqg5y3yDEcsW/Tqrr8UMAnHcSDOEJKnGqSd69zgFk5+gIDDB8TMGRGTD3RyFDCmI4t4CTHsy/S9IvGnnfAhEMqPfGGKGGBEAKCwHhiAAtOSbPgDkWnCjjLFtmXTPHyI51X2j8NNTIdILvTOseLAHGRGNfK3/ctIhYvNo1J6KTnRQhFiiNTZw9lmoyHRW+mWluNqagmi5VwZE9q44/BZOHXsOHIDfjXnauDNqJ+vgBTU0YuzLhFagAAAAIABwGAQHfBwFoYgAd6kMCs2ert0lIY5Uvr72UrHm131fN4Cbfqcu6l8JN6qAhOx+YAAAAAAAAAAAAAAAAAQgBsWgALTkmz4A5Fpwo4yxbZl0zx8iOdV9o/DTUyHSC70zrHi0ADvUhgVmz1dukpDHKl9feylY82u+r5vATb9Tl3UvhJvVQEJ2PzAYQRrQAAFU6xlYrBMy4Q1TACAGsD4p+pVRt5O+WkF0hUC4IWjCIAMY4y5aWZKh9J0ROMzXgh7NRceqEx2ecMET/roAVDBGPAAWnJNnwByLThRxli2zLpnj5Ec6r7R+GmpkOkF3pnWPFggMJABwAAAAAMTk4NTU5NzY1MQCdQZ2DE4gAAAAAAAAAABEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIABvyYYagEwQRpgAAAAAAAIAAAAAAAKbaMIgTubTOVgZmmnnD3EI+NwAJ4mOobPF4MErurqSsEDQLwyJIber",
 *       "transaction_id": {
 *         "@type": "internal.transactionId",
 *         "lt": "46855462000001",
 *         "hash": "t0qb88CPdXbosDWQnIKph3gvo7j/6UsO8juQ0ysBUno="
 *       },
 *       "fee": "3171551",
 *       "storage_fee": "41951",
 *       "other_fee": "3129600",
 *       "in_msg": {
 *         "@type": "raw.message",
 *         "source": "",
 *         "destination": "EQAWnJNnwByLThRxli2zLpnj5Ec6r7R-GmpkOkF3pnWPFuF-",
 *         "value": "0",
 *         "fwd_fee": "0",
 *         "ihr_fee": "0",
 *         "created_lt": "0",
 *         "body_hash": "wNmdqMzrfzJ/o8qxWdXuGwli/YcRz+nXzLwZ0L2jzAE=",
 *         "msg_data": {
 *           "@type": "msg.dataRaw",
 *           "body": "te6cckEBBAEA8QABnDjIjGvlb/uWkQsXm0ak9FJzooQixRGps4eyzUZDorfTLS3G1NQTRcq4Mie1ccfgsnDr2HDkBvxrztXBm1E/XwApqaMXZlwitQAAAAEAAwEBaGIAHepDArNnq7dJSGOVL6+9lKx5td9XzeAm36nLupfCTeqgITsfmAAAAAAAAAAAAAAAAAECAawPin6lVG3k75aQXSFQLghaMIgAxjjLlpZkqH0nRE4zNeCHs1Fx6oTHZ5wwRP+ugBUMEY8ABack2fAHItOFHGWLbMumePkRzqvtH4aamQ6QXemdY8WCAwMAHAAAAAAxOTg1NTk3NjUxPDejzQ==",
 *           "init_state": ""
 *         },
 *         "message": "OMiMa+Vv+5aRCxebRqT0UnOihCLFEamzh7LNRkOit9MtLcbU1BNFyrgyJ7Vxx+CycOvYcOQG/GvO\n1cGbUT9fACmpoxdmXCK1AAAAAQAD\n"
 *       },
 *       "out_msgs": [
 *         {
 *           "@type": "raw.message",
 *           "source": "EQAWnJNnwByLThRxli2zLpnj5Ec6r7R-GmpkOkF3pnWPFuF-",
 *           "destination": "EQA71IYFZs9XbpKQxypfX3spWPNrvq-bwE2_U5d1L4Sb1Wnx",
 *           "value": "69690355",
 *           "fwd_fee": "533338",
 *           "ihr_fee": "0",
 *           "created_lt": "46855462000002",
 *           "body_hash": "CXScTXWvTTsX5O2GrmkxgrzG6mI204tqVXlchvhjSLw=",
 *           "msg_data": {
 *             "@type": "msg.dataRaw",
 *             "body": "te6cckEBAgEAaQABrA+KfqVUbeTvlpBdIVAuCFowiADGOMuWlmSofSdETjM14IezUXHqhMdnnDBE/66AFQwRjwAFpyTZ8Aci04UcZYtsy6Z4+RHOq+0fhpqZDpBd6Z1jxYIDAQAcAAAAADE5ODU1OTc2NTF/kQYP",
 *             "init_state": ""
 *           },
 *           "message": "D4p+pVRt5O+WkF0hUC4IWjCIAMY4y5aWZKh9J0ROMzXgh7NRceqEx2ecMET/roAVDBGPAAWnJNnw\nByLThRxli2zLpnj5Ec6r7R+GmpkOkF3pnWPFggM=\n"
 *         }
 *       ]
 *     },
 *     {
 *       "@type": "raw.transaction",
 *       "address": {
 *         "@type": "accountAddress",
 *         "account_address": "EQAWnJNnwByLThRxli2zLpnj5Ec6r7R-GmpkOkF3pnWPFuF-"
 *       },
 *       "utime": 1717149226,
 *       "data": "te6cckECHwEABWUAA7Vxack2fAHItOFHGWLbMumePkRzqvtH4aamQ6QXemdY8WAAAqlQ9ZPAHk8sCzGddkEXU+MNqo156q5kmEZ073tMyxh172VcZr6QAAKodR9ZMDZlmeKgACRp4UJIAQIDAgHgBAUAgnLDqW0JMzXhkblSB8YxwvcUqxAThas/1kV71WPM4ZgqslNO5aaqDnLfIMRyxb9OquvxQwCcdxIM4QkqcapJ3r3OAhEMgofGGKGGBEAdHgPjiAAtOSbPgDkWnCjjLFtmXTPHyI51X2j8NNTIdILvTOseLBGG2vYjHv6+OEtwJq5I83AoOv4ezDTavISSgw7xWyX+ATVwpU5zgbDMln2pUzLJYPnN71vz1c4oG48ttQw/fEmBBTU0Yv/////gAAAAAABwBgcIAQHfHAEU/wD0pBP0vPLICwkAUQAAAAApqaMXT1z2lABlLP0JGTk6rcnlWN05O2bz3RRSU4SSLlur8PFAAIJCADGOMuWlmSofSdETjM14IezUXHqhMdnnDBE/66AFQwRjnd13AAAAAAAAAAAAAAAAAAAAAAAAMTk4NTU5NzY1MQIBIAoLAgFIDA0E+PKDCNcYINMf0x/THwL4I7vyZO1E0NMf0x/T//QE0VFDuvKhUVG68qIF+QFUEGT5EPKj+AAkpMjLH1JAyx9SMMv/UhD0AMntVPgPAdMHIcAAn2xRkyDXSpbTB9QC+wDoMOAhwAHjACHAAuMAAcADkTDjDQOkyMsfEssfy/8YGRobAubQAdDTAyFxsJJfBOAi10nBIJJfBOAC0x8hghBwbHVnvSKCEGRzdHK9sJJfBeAD+kAwIPpEAcjKB8v/ydDtRNCBAUDXIfQEMFyBAQj0Cm+hMbOSXwfgBdM/yCWCEHBsdWe6kjgw4w0DghBkc3RyupJfBuMNDg8CASAQEQB4AfoA9AQw+CdvIjBQCqEhvvLgUIIQcGx1Z4MesXCAGFAEywUmzxZY+gIZ9ADLaRfLH1Jgyz8gyYBA+wAGAIpQBIEBCPRZMO1E0IEBQNcgyAHPFvQAye1UAXKwjiOCEGRzdHKDHrFwgBhQBcsFUAPPFiP6AhPLassfyz/JgED7AJJfA+ICASASEwBZvSQrb2omhAgKBrkPoCGEcNQICEekk30pkQzmkD6f+YN4EoAbeBAUiYcVnzGEAgFYFBUAEbjJftRNDXCx+AA9sp37UTQgQFA1yH0BDACyMoHy//J0AGBAQj0Cm+hMYAIBIBYXABmtznaiaEAga5Drhf/AABmvHfaiaEAQa5DrhY/AAG7SB/oA1NQi+QAFyMoHFcv/ydB3dIAYyMsFywIizxZQBfoCFMtrEszMyXP7AMhAFIEBCPRR8qcCAHCBAQjXGPoA0z/IVCBHgQEI9FHyp4IQbm90ZXB0gBjIywXLAlAGzxZQBPoCFMtqEssfyz/Jc/sAAgBsgQEI1xj6ANM/MFIkgQEI9Fnyp4IQZHN0cnB0gBjIywXLAlAFzxZQA/oCE8tqyx8Syz/Jc/sAAAr0AMntVADLSAAtOSbPgDkWnCjjLFtmXTPHyI51X2j8NNTIdILvTOseLQAYxxly0syVD6ToicZmvBD2ai49UJjs84YIn/XQAqGCMc7uu4AGCCNaAABVKh6yeATMszxUAAAAABicnBqanJubGpjAAJ1BnYMTiAAAAAAAAAAAEQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAgAG/Jgw1ATAgjTAAAAAAAAgAAAAAAA9jtb/H83CbdoWwPJq9Q2l9oOmyroYX1sbHi+vuJpGXeQFAZTNdTwsQ=",
 *       "transaction_id": {
 *         "@type": "internal.transactionId",
 *         "lt": "46819696000001",
 *         "hash": "vA0OjZEucsuHQpKoAP5HijRbQFH0NicCDSUbk2LgTvw="
 *       },
 *       "fee": "5446591",
 *       "storage_fee": "2591",
 *       "other_fee": "5444000",
 *       "in_msg": {
 *         "@type": "raw.message",
 *         "source": "",
 *         "destination": "EQAWnJNnwByLThRxli2zLpnj5Ec6r7R-GmpkOkF3pnWPFuF-",
 *         "value": "0",
 *         "fwd_fee": "0",
 *         "ihr_fee": "0",
 *         "created_lt": "0",
 *         "body_hash": "Xl6mOZwQydgxRePjGDYUmRrBMRM6E3rYHFjmR6v299o=",
 *         "msg_data": {
 *           "@type": "msg.dataRaw",
 *           "body": "te6cckEBAgEAlAABnDbXsRj39fHCW4E1ckebgUHX8PZhptXkJJQYd4rZL/AJq4Uqc5wNhmSz7UqZlksHzm96356ucUDceW2oYfviTAgpqaMX/////wAAAAAAAwEAgkIAMY4y5aWZKh9J0ROMzXgh7NRceqEx2ecMET/roAVDBGOd3XcAAAAAAAAAAAAAAAAAAAAAAAAxOTg1NTk3NjUxGoHElQ==",
 *           "init_state": "te6cckECFgEAAwQAAgE0AQIBFP8A9KQT9LzyyAsDAFEAAAAAKamjF09c9pQAZSz9CRk5Oq3J5VjdOTtm890UUlOEki5bq/DxQAIBIAQFAgFIBgcE+PKDCNcYINMf0x/THwL4I7vyZO1E0NMf0x/T//QE0VFDuvKhUVG68qIF+QFUEGT5EPKj+AAkpMjLH1JAyx9SMMv/UhD0AMntVPgPAdMHIcAAn2xRkyDXSpbTB9QC+wDoMOAhwAHjACHAAuMAAcADkTDjDQOkyMsfEssfy/8SExQVAubQAdDTAyFxsJJfBOAi10nBIJJfBOAC0x8hghBwbHVnvSKCEGRzdHK9sJJfBeAD+kAwIPpEAcjKB8v/ydDtRNCBAUDXIfQEMFyBAQj0Cm+hMbOSXwfgBdM/yCWCEHBsdWe6kjgw4w0DghBkc3RyupJfBuMNCAkCASAKCwB4AfoA9AQw+CdvIjBQCqEhvvLgUIIQcGx1Z4MesXCAGFAEywUmzxZY+gIZ9ADLaRfLH1Jgyz8gyYBA+wAGAIpQBIEBCPRZMO1E0IEBQNcgyAHPFvQAye1UAXKwjiOCEGRzdHKDHrFwgBhQBcsFUAPPFiP6AhPLassfyz/JgED7AJJfA+ICASAMDQBZvSQrb2omhAgKBrkPoCGEcNQICEekk30pkQzmkD6f+YN4EoAbeBAUiYcVnzGEAgFYDg8AEbjJftRNDXCx+AA9sp37UTQgQFA1yH0BDACyMoHy//J0AGBAQj0Cm+hMYAIBIBARABmtznaiaEAga5Drhf/AABmvHfaiaEAQa5DrhY/AAG7SB/oA1NQi+QAFyMoHFcv/ydB3dIAYyMsFywIizxZQBfoCFMtrEszMyXP7AMhAFIEBCPRR8qcCAHCBAQjXGPoA0z/IVCBHgQEI9FHyp4IQbm90ZXB0gBjIywXLAlAGzxZQBPoCFMtqEssfyz/Jc/sAAgBsgQEI1xj6ANM/MFIkgQEI9Fnyp4IQZHN0cnB0gBjIywXLAlAFzxZQA/oCE8tqyx8Syz/Jc/sAAAr0AMntVLG3KVk="
 *         },
 *         "message": "NtexGPf18cJbgTVyR5uBQdfw9mGm1eQklBh3itkv8AmrhSpznA2GZLPtSpmWSwfOb3rfnq5xQNx5\nbahh++JMCCmpoxf/////AAAAAAAD\n"
 *       },
 *       "out_msgs": [
 *         {
 *           "@type": "raw.message",
 *           "source": "EQAWnJNnwByLThRxli2zLpnj5Ec6r7R-GmpkOkF3pnWPFuF-",
 *           "destination": "EQBjHGXLSzJUPpOiJxma8EPZqLj1QmOzzhgif9dACoYIx6UV",
 *           "value": "12300000",
 *           "fwd_fee": "266669",
 *           "ihr_fee": "0",
 *           "created_lt": "46819696000002",
 *           "body_hash": "9JEOs0WWtr+Mr8hQOCnU+1TQ5UwlYNwwnubB51ggQG8=",
 *           "msg_data": {
 *             "@type": "msg.dataText",
 *             "text": "MTk4NTU5NzY1MQ=="
 *           },
 *           "message": "1985597651"
 *         }
 *       ]
 *     },
 *     {
 *       "@type": "raw.transaction",
 *       "address": {
 *         "@type": "accountAddress",
 *         "account_address": "EQAWnJNnwByLThRxli2zLpnj5Ec6r7R-GmpkOkF3pnWPFuF-"
 *       },
 *       "utime": 1716867715,
 *       "data": "te6cckECBgEAAToAA7Fxack2fAHItOFHGWLbMumePkRzqvtH4aamQ6QXemdY8WAAAqh1H1kwP9lPyHJ/CgdW5KqxkBr+MVnalIU1h8ISp3kblqnc23PgAAKodRx8xDZlVSgwAAAgKAECAwEBoAQAgnJNDD2c2gQPBNTdJPktm1ODQdh+Wr7W58Q+qSrXFMCAn8OpbQkzNeGRuVIHxjHC9xSrEBOFqz/WRXvVY8zhmCqyAAsMQEhASSABq0gA3sXk9REMT0i1czySGpcGsyZR96MlVYbo3CyoWTxdxOUABack2fAHItOFHGWLbMumePkRzqvtH4aamQ6QXemdY8WEBAYMNQgAAFUOo+smBMyqpQbABQBkc2LQnAAAAAAAKCo1QBA2ZAgAPrANjnoHd5Ldb91+LAz+Pl0cThn0LargA1U9Xuit4BA23iKC",
 *       "transaction_id": {
 *         "@type": "internal.transactionId",
 *         "lt": "46760684000003",
 *         "hash": "5PLAsxnXZBF1PjDaqNeequZJhGdO97TMsYde9lXGa+k="
 *       },
 *       "fee": "1",
 *       "storage_fee": "1",
 *       "other_fee": "0",
 *       "in_msg": {
 *         "@type": "raw.message",
 *         "source": "EQBvYvJ6iIYnpFq5nkkNS4NZkyj70ZKqw3RuFlQsni7icgjJ",
 *         "destination": "EQAWnJNnwByLThRxli2zLpnj5Ec6r7R-GmpkOkF3pnWPFuF-",
 *         "value": "1",
 *         "fwd_fee": "400004",
 *         "ihr_fee": "0",
 *         "created_lt": "46760684000002",
 *         "body_hash": "DNDvf4Tmvf94XYP68CL8C0x2cJT+bWDOS7Kb1RdV/5k=",
 *         "msg_data": {
 *           "@type": "msg.dataRaw",
 *           "body": "te6cckEBAQEANAAAZHNi0JwAAAAAACgqNUAQNmQIAD6wDY56B3eS3W/dfiwM/j5dHE4Z9C2q4ANVPV7oreAQFEMc4A==",
 *           "init_state": ""
 *         },
 *         "message": "c2LQnAAAAAAAKCo1QBA2ZAgAPrANjnoHd5Ldb91+LAz+Pl0cThn0LargA1U9Xuit4BA=\n"
 *       },
 *       "out_msgs": []
 *     },
 *     {
 *       "@type": "raw.transaction",
 *       "address": {
 *         "@type": "accountAddress",
 *         "account_address": "EQAWnJNnwByLThRxli2zLpnj5Ec6r7R-GmpkOkF3pnWPFuF-"
 *       },
 *       "utime": 1716867704,
 *       "data": "te6cckECBgEAATkAA7Fxack2fAHItOFHGWLbMumePkRzqvtH4aamQ6QXemdY8WAAAqh1HHzEPwC/jJ0g2sD8Yangm1IrgQtOP9HXjdIbO089xBmpeU6AAAKodP/gjDZlVSeAAAAgSAECAwEBoAQAgnL3mu01ose6A9NZ5XEkVVnpWc7A0Ku2aVYbAbrpy4n4O00MPZzaBA8E1N0k+S2bU4NB2H5avtbnxD6pKtcUwICfAAsMQIhASSABq0gA3sXk9REMT0i1czySGpcGsyZR96MlVYbo3CyoWTxdxOUABack2fAHItOFHGWLbMumePkRzqvtH4aamQ6QXemdY8WEBAYMJFwAAFUOo4+YhMyqpPDABQBic2LQnAAAAAAAKCnMOYloCAA+sA2Oegd3kt1v3X4sDP4+XRxOGfQtquADVT1e6K3gEN4rJWE=",
 *       "transaction_id": {
 *         "@type": "internal.transactionId",
 *         "lt": "46760681000003",
 *         "hash": "/ZT8hyfwoHVuSqsZAa/jFZ2pSFNYfCEqd5G5ap3Ntz4="
 *       },
 *       "fee": "2",
 *       "storage_fee": "2",
 *       "other_fee": "0",
 *       "in_msg": {
 *         "@type": "raw.message",
 *         "source": "EQBvYvJ6iIYnpFq5nkkNS4NZkyj70ZKqw3RuFlQsni7icgjJ",
 *         "destination": "EQAWnJNnwByLThRxli2zLpnj5Ec6r7R-GmpkOkF3pnWPFuF-",
 *         "value": "1",
 *         "fwd_fee": "397870",
 *         "ihr_fee": "0",
 *         "created_lt": "46760681000002",
 *         "body_hash": "Wvf65naq/mmxYt95f4fZdFTkrT/DuPiHPMFIJANpyAw=",
 *         "msg_data": {
 *           "@type": "msg.dataRaw",
 *           "body": "te6cckEBAQEAMwAAYnNi0JwAAAAAACgpzDmJaAgAPrANjnoHd5Ldb91+LAz+Pl0cThn0LargA1U9Xuit4BCtUifQ",
 *           "init_state": ""
 *         },
 *         "message": "c2LQnAAAAAAAKCnMOYloCAA+sA2Oegd3kt1v3X4sDP4+XRxOGfQtquADVT1e6K3gEA==\n"
 *       },
 *       "out_msgs": []
 *     },
 *     {
 *       "@type": "raw.transaction",
 *       "address": {
 *         "@type": "accountAddress",
 *         "account_address": "EQAWnJNnwByLThRxli2zLpnj5Ec6r7R-GmpkOkF3pnWPFuF-"
 *       },
 *       "utime": 1716867575,
 *       "data": "te6cckECBgEAATsAA7Fxack2fAHItOFHGWLbMumePkRzqvtH4aamQ6QXemdY8WAAAqh0/+CMO1U/n2zF6ppoDtLbVM3L6Dqj+rKdR3xvtI8Wr37gOPTwAAKodOvZmDZlVR9wAAAgKAECAwEBoAQAgnLRGA3KjFWz04W7vCJnJlBUEQ+5eJZTrgSn1tLUoAcPF/ea7TWix7oD01nlcSRVWelZzsDQq7ZpVhsBuunLifg7AAsMQEhASSABq0gAd6kMCs2ert0lIY5Uvr72UrHm131fN4Cbfqcu6l8JN6sABack2fAHItOFHGWLbMumePkRzqvtH4aamQ6QXemdY8WEBAYMRbIAAFUOn/wRhMyqo+7ABQBmc2LQnAAAAAAAKClDUXSHboAIADaJ2KpsLWHIqiBuBxBpe+F7VD4IgcIeZmOzCCsKwlTQRqiRgg==",
 *       "transaction_id": {
 *         "@type": "internal.transactionId",
 *         "lt": "46760651000003",
 *         "hash": "8Av4ydINrA/GGp4JtSK4ELTj/R143SGztPPcQZqXlOg="
 *       },
 *       "fee": "1",
 *       "storage_fee": "1",
 *       "other_fee": "0",
 *       "in_msg": {
 *         "@type": "raw.message",
 *         "source": "EQA71IYFZs9XbpKQxypfX3spWPNrvq-bwE2_U5d1L4Sb1Wnx",
 *         "destination": "EQAWnJNnwByLThRxli2zLpnj5Ec6r7R-GmpkOkF3pnWPFuF-",
 *         "value": "1",
 *         "fwd_fee": "402137",
 *         "ihr_fee": "0",
 *         "created_lt": "46760651000002",
 *         "body_hash": "H+jEtE2zA/KMjSoNc4COf0AD4ZJ904o6emXBfH5i7IU=",
 *         "msg_data": {
 *           "@type": "msg.dataRaw",
 *           "body": "te6cckEBAQEANQAAZnNi0JwAAAAAACgpQ1F0h26ACAA2idiqbC1hyKogbgcQaXvhe1Q+CIHCHmZjswgrCsJU0JZT05Y=",
 *           "init_state": ""
 *         },
 *         "message": "c2LQnAAAAAAAKClDUXSHboAIADaJ2KpsLWHIqiBuBxBpe+F7VD4IgcIeZmOzCCsKwlTQ\n"
 *       },
 *       "out_msgs": []
 *     },
 *     {
 *       "@type": "raw.transaction",
 *       "address": {
 *         "@type": "accountAddress",
 *         "account_address": "EQAWnJNnwByLThRxli2zLpnj5Ec6r7R-GmpkOkF3pnWPFuF-"
 *       },
 *       "utime": 1716867478,
 *       "data": "te6cckECBQEAAQ0AA69xack2fAHItOFHGWLbMumePkRzqvtH4aamQ6QXemdY8WAAAqh069mYMAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAZlVRlgABgIAQIDAQGgBACCcpCuyJZa+rsW68PLm0COuucbYY14eIvIDQmENZPKyY2k0RgNyoxVs9OFu7wiZyZQVBEPuXiWU64Ep9bS1KAHDxcADwwJBHhowAEgALlIAJwP2wkJkuwWqw0Zerj7oZ67fMQGUUZIUH32LaOZqr8TAAWnJNnwByLThRxli2zLpnj5Ec6r7R+GmpkOkF3pnWPFkEeGjAAGCCNaAABVDp17MwTMqqMsAAAAAEDcd+++",
 *       "transaction_id": {
 *         "@type": "internal.transactionId",
 *         "lt": "46760630000003",
 *         "hash": "tVP59sxeqaaA7S21TNy+g6o/qynUd8b7SPFq9+4Dj08="
 *       },
 *       "fee": "0",
 *       "storage_fee": "0",
 *       "other_fee": "0",
 *       "in_msg": {
 *         "@type": "raw.message",
 *         "source": "EQBOB-2EhMl2C1WGjL1cfdDPXb5iAyijJCg--xbRzNVfiX-L",
 *         "destination": "EQAWnJNnwByLThRxli2zLpnj5Ec6r7R-GmpkOkF3pnWPFuF-",
 *         "value": "300000000",
 *         "fwd_fee": "266669",
 *         "ihr_fee": "0",
 *         "created_lt": "46760630000002",
 *         "body_hash": "P+k4lxWGmOTUc7dEFNdJNxaw/DpwMQk0hz8AGdqsyrQ=",
 *         "msg_data": {
 *           "@type": "msg.dataText",
 *           "text": ""
 *         },
 *         "message": ""
 *       },
 *       "out_msgs": []
 *     }
 *   ]
 * }
 * </pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TonCenterV2GetTransactions implements Serializable {


}
