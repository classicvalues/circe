package io.circe.generic

import io.circe.Decoder
import shapeless.{ HList, LabelledGeneric }, shapeless.ops.function.FnFromProduct

trait IncompleteInstances {
  implicit def decodeIncompleteCaseClass[F, P <: HList, A, T <: HList, R <: HList](implicit
    ffp: FnFromProduct.Aux[P => A, F],
    gen: LabelledGeneric.Aux[A, T],
    complement: Complement.Aux[T, P, R],
    d: Decoder[R]
  ): Decoder[F] = d.map(r => ffp(p => gen.from(complement.insert(p, r))))

  implicit def decodeCaseClassPatch[A, R <: HList, O <: HList](implicit
    gen: LabelledGeneric.Aux[A, R],
    patch: PatchWithOptions.Aux[R, O],
    d: Decoder[O]
  ): Decoder[A => A] = d.map(o => a => gen.from(patch(gen.to(a), o)))
}
